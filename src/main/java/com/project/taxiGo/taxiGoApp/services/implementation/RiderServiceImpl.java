package com.project.taxiGo.taxiGoApp.services.implementation;

import com.project.taxiGo.taxiGoApp.dto.DriverDTO;
import com.project.taxiGo.taxiGoApp.dto.RideRequestDTO;
import com.project.taxiGo.taxiGoApp.dto.RiderDTO;
import com.project.taxiGo.taxiGoApp.dto.RiderRideDTO;
import com.project.taxiGo.taxiGoApp.entities.*;
import com.project.taxiGo.taxiGoApp.enums.RideRequestStatus;
import com.project.taxiGo.taxiGoApp.enums.RideStatus;
import com.project.taxiGo.taxiGoApp.exceptions.ResourceNotFoundException;
import com.project.taxiGo.taxiGoApp.repositories.RideRequestRepository;
import com.project.taxiGo.taxiGoApp.repositories.RiderRepository;
import com.project.taxiGo.taxiGoApp.services.*;
import com.project.taxiGo.taxiGoApp.strategies.RideStrategyManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepo;
    private final RiderRepository riderRepo;
    private final DistanceService distanceService;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;
    private final EmailSendingService emailService;

    @Override
    @Transactional
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {

        Rider rider = getCurrentRider();

        // convert provided rideRequestDTO to rideRequestEntity using model mapper
        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);

        // set rideStatus to pending
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);
        Point pickupLocation = modelMapper.map(rideRequestDTO.getPickupLocation(), Point.class);
        Point dropOffLocation = modelMapper.map(rideRequestDTO.getDropOffLocation(), Point.class);
        rideRequest.setDistance(distanceService.calculateDistance(pickupLocation, dropOffLocation));

        // calculate fare related to the ride request
        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);


        // saved ride request to db
        RideRequest savedRideRequest = rideRequestRepo.save(rideRequest);

        // match driver for the ride
        List<Driver> drivers = rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);
//        log.info(driver.toString());

        String[] emails = (String[]) drivers.stream().map(driver -> driver.getUser().getEmail()).toArray();

        emailService.sendBulkEmail(emails, "New Ride Request!", "New Ride request from "+rider);

//        log.info(rideRequestEntity.toString());

        return modelMapper.map(savedRideRequest, RideRequestDTO.class);
    }

    @Override
    public RiderRideDTO cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider does not own this ride!");
        }
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(), true);
        return modelMapper.map(savedRide, RiderRideDTO.class);
    }

    @Override
    public DriverDTO rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider cannot rate the driver, as the ride with id "+rideId+" is not associated to them!");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Rider cannot rate driver, ride status is not ENDED. Ride status: "+ride.getRideStatus());
        }

        return ratingService.rateDriver(ride, rating);
    }

    @Override
    public RiderDTO getMyProfile() {
        Rider rider = getCurrentRider();
        return modelMapper.map(rider, RiderDTO.class);
    }

    @Override
    public Page<RiderRideDTO> getAllRides(PageRequest pageRequest) {
        return rideService.getAllRidesOfRider(getCurrentRider(), pageRequest).map(
                rideEntity -> modelMapper.map(rideEntity, RiderRideDTO.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider.builder()
                .user(user)
                .rating(0.0)
                .build();

        return riderRepo.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return riderRepo.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("User is not a rider! User Id: "+user.getId()));
    }

//    @Override
//    public RiderEntity updateRiderRating(RiderEntity rider, Integer rating) {
//        Page<RiderRideDTO> rides = getAllRides(PageRequest.of(0, Integer.MAX_VALUE));
//        long totalRides = rides.getTotalElements();
//        double currentRating = rider.getRating();
//        rider.setRating(((currentRating*totalRides)+rating)/(totalRides+1));
//        return riderRepo.save(rider);
//    }

}
