package com.project.taxiGo.taxiGoApp.services;

import com.project.taxiGo.taxiGoApp.dto.DriverDTO;
import com.project.taxiGo.taxiGoApp.dto.RideRequestDTO;
import com.project.taxiGo.taxiGoApp.dto.RiderDTO;
import com.project.taxiGo.taxiGoApp.dto.RiderRideDTO;
import com.project.taxiGo.taxiGoApp.entities.Rider;
import com.project.taxiGo.taxiGoApp.entities.User;
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
