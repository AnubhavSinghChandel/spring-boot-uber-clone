package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.DriverDTO;
import com.project.uber.uberApp.dto.DriverRideDTO;
import com.project.uber.uberApp.dto.RideDTO;
import com.project.uber.uberApp.dto.RiderDTO;
import com.project.uber.uberApp.entities.DriverEntity;
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

    DriverEntity getCurrentDriver();

//    DriverEntity updateDriverRating(DriverEntity driver, Integer rating);

    DriverEntity updateDriverAvailability(DriverEntity driver, boolean flag);

    DriverEntity createNewDriver(DriverEntity driver);
}
