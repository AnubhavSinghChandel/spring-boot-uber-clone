package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.dto.DriverDTO;
import com.project.uber.uberApp.dto.RiderDTO;
import com.project.uber.uberApp.entities.Driver;
import com.project.uber.uberApp.entities.Rating;
import com.project.uber.uberApp.entities.Ride;
import com.project.uber.uberApp.entities.Rider;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.project.uber.uberApp.repositories.DriverRepository;
import com.project.uber.uberApp.repositories.RatingRepository;
import com.project.uber.uberApp.repositories.RideRepository;
import com.project.uber.uberApp.repositories.RiderRepository;
import com.project.uber.uberApp.services.RatingService;
import com.project.uber.uberApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final ModelMapper modelMapper;
    private final RiderRepository riderRepo;
    private final DriverRepository driverRepo;
    private final RideService rideService;
    private final RideRepository rideRepo;
    private final RatingRepository ratingRepo;

    @Override
    public DriverDTO rateDriver(Ride ride, Integer rating) {
        Rating rideRating = getRideRating(ride);
        if(rideRating.getDriverRating() != null){
            throw new RuntimeConflictException("The driver has already been rated for the ride with id: "+ride.getId());
        }
        Driver driver = ride.getDriver();
        rideRating.setDriverRating(rating);
        ratingRepo.save(rideRating);
        Page<Ride> rides = rideService.getAllRidesOfDriver(driver, PageRequest.of(0, Integer.MAX_VALUE));
        long totalRides = rides.getTotalElements();
        double currentRating = driver.getRating();
        driver.setRating(((currentRating*totalRides)+rating)/(totalRides+1));
        Driver savedDriver = driverRepo.save(driver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public RiderDTO rateRider(Ride ride, Integer rating) {
        Rating rideRating = getRideRating(ride);
        if(rideRating.getRiderRating() != null){
            throw new RuntimeConflictException("The rider has already been rated for the ride with id: "+ride.getId());
        }
        Rider rider = ride.getRider();
        rideRating.setRiderRating(rating);
        rideRepo.save(ride);
        Page<Ride> rides = rideService.getAllRidesOfRider(rider, PageRequest.of(0, Integer.MAX_VALUE));
        long totalRides = rides.getTotalElements();
        double currentRating = rider.getRating();
        rider.setRating(((currentRating*totalRides)+rating)/(totalRides+1));
        Rider savedRider = riderRepo.save(rider);
        return modelMapper.map(savedRider, RiderDTO.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .ride(ride)
                .build();
        ratingRepo.save(rating);
    }

    public Rating getRideRating(Ride ride){
        return ratingRepo.findByRide(ride)
                .orElseThrow(()->new ResourceNotFoundException("Rating for ride with id: "+ride.getId()+" does not exist!"));
    }
}
