package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.*;
import com.project.uber.uberApp.entities.RiderEntity;
import com.project.uber.uberApp.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RiderRideDTO cancelRide(Long rideId);

    DriverDTO rateDriver(Long rideId, Integer rating);

    RiderDTO getMyProfile();

    Page<RiderRideDTO> getAllRides(PageRequest pageRequest);

    RiderEntity createNewRider(UserEntity userEntity);

    RiderEntity getCurrentRider();

//    RiderEntity updateRiderRating(RiderEntity rider, Integer rating);
}
