package com.project.uber.uberApp.services.implementation;


import com.project.uber.uberApp.dto.DriverDTO;
import com.project.uber.uberApp.dto.DriverRideDTO;
import com.project.uber.uberApp.dto.RiderDTO;
import com.project.uber.uberApp.entities.*;
import com.project.uber.uberApp.enums.RideRequestStatus;
import com.project.uber.uberApp.enums.RideStatus;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.repositories.DriverRepository;
import com.project.uber.uberApp.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepo;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    @Override
    @Transactional
    public DriverRideDTO acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw new RuntimeException("Ride request cannot be accepted, status is: "+rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
            throw new RuntimeException("Driver cannot accept ride due to unavailability");
        }

        Driver savedDriver = updateDriverAvailability(currentDriver, false);


        Ride ride = rideService.createNewRide(rideRequest, savedDriver);
        return modelMapper.map(ride, DriverRideDTO.class);
    }

    @Override
    public DriverRideDTO cancelRide(Long rideId) {
        // Driver can only cancel ride when the ride has been accepted but not yet started
        Ride ride = rideService.getRideById(rideId);

//        easiest way to check for ride Status and display failure of cancel

//        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
//            throw new RuntimeException("Ride with id "+rideId+" cannot be canceled");
//        }

//        Detailed error for canceling failure

//        if(ride.getRideStatus().equals(RideStatus.ENDED)){
//            throw new RuntimeException("The ride with id "+rideId+" cannot be cancelled as it is has ended!");
//        }else if(ride.getRideStatus().equals(RideStatus.ONGOING)){
//            throw new RuntimeException("The ride with id "+rideId+" cannot be cancelled as it is ongoing!");
//        } else if (ride.getRideStatus().equals(RideStatus.CANCELLED)) {
//            throw new RuntimeException("The ride with id "+rideId+" cannot be cancelled as it is has already been canceled!");
//        }

//        switch for detailed error

        switch (ride.getRideStatus()){
            case RideStatus.ENDED -> throw new RuntimeException("The ride with id "+rideId+" cannot be cancelled as it is has ended!");
            case RideStatus.ONGOING -> throw new RuntimeException("The ride with id "+rideId+" cannot be cancelled as it is ongoing!");
            case RideStatus.CANCELLED -> throw new RuntimeException("The ride with id "+rideId+" cannot be cancelled as it's already been canceled!");
        }
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot cancel ride, as the driver is not the one who accepted it.");
        }
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        updateDriverAvailability(driver, true);
        return modelMapper.map(savedRide, DriverRideDTO.class);
    }

    @Override
    public DriverRideDTO startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride, as they did not accept the ride earlier!");
        }
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Driver cannot start ride, ride status is not CONFIRMED. Ride status: "+ride.getRideStatus());
        }
        if(!ride.getOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }
        ride.setStartTime(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(ride);
        return modelMapper.map(savedRide, DriverRideDTO.class);
    }

    @Override
    @Transactional
    public DriverRideDTO endRide(Long rideId) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot end the ride, as they did not start the ride!");
        }
        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Driver cannot end ride, ride status is not ONGOING. Ride status: "+ride.getRideStatus());
        }

        ride.setEndTime(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
        updateDriverAvailability(driver, true);

        paymentService.processPayment(ride);

        return modelMapper.map(savedRide, DriverRideDTO.class);
    }

    @Override
    public RiderDTO rateRider(Long rideId, Integer rating) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot rate the rider, as they are not the driver assigned to the ride!");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Driver cannot rate rider, ride status is not ENDED. Ride status: "+ride.getRideStatus());
        }

        return ratingService.rateRider(ride, rating);
    }

    @Override
    public DriverDTO getMyProfile() {
        Driver driver = getCurrentDriver();
        return modelMapper.map(driver, DriverDTO.class);
    }

    @Override
    public Page<DriverRideDTO> getAllRides(PageRequest page) {
        return rideService.getAllRidesOfDriver(getCurrentDriver(), page).map(
                rideEntity -> modelMapper.map(rideEntity, DriverRideDTO.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driverRepo.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("User is not a driver! User id: "+user.getId()));
    }

//    @Override
//    public DriverEntity updateDriverRating(DriverEntity driver, Integer rating) {
//        Page<DriverRideDTO> rides = getAllRides(PageRequest.of(0, Integer.MAX_VALUE));
//        long totalRides = rides.getTotalElements();
//        double currentRating = driver.getRating();
//        driver.setRating(((currentRating*totalRides)+rating)/(totalRides+1)); //weighted average formula
//        return driverRepo.save(driver);
//    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean flag) {
        driver.setAvailable(flag);
        return driverRepo.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return  driverRepo.save(driver);
    }
}
