package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.DriverDTO;
import com.project.uber.uberApp.dto.DriverRideDTO;
import com.project.uber.uberApp.dto.RiderDTO;
import com.project.uber.uberApp.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {

    DriverRideDTO acceptRide(Long rideRequestId);

    DriverRideDTO cancelRide(Long rideId);

    DriverRideDTO startRide(Long rideId, String otp);

    DriverRideDTO endRide(Long rideId);

    RiderDTO rateRider(Long rideId, Integer rating);

    DriverDTO getMyProfile();

    Page<DriverRideDTO> getAllRides(PageRequest page);

    Driver getCurrentDriver();

//    DriverEntity updateDriverRating(DriverEntity driver, Integer rating);

    Driver updateDriverAvailability(Driver driver, boolean flag);

    Driver createNewDriver(Driver driver);
}
