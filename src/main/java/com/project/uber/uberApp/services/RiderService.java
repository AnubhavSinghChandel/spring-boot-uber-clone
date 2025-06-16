package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.*;
import com.project.uber.uberApp.entities.Rider;
import com.project.uber.uberApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RiderRideDTO cancelRide(Long rideId);

    DriverDTO rateDriver(Long rideId, Integer rating);

    RiderDTO getMyProfile();

    Page<RiderRideDTO> getAllRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();

//    RiderEntity updateRiderRating(RiderEntity rider, Integer rating);
}
