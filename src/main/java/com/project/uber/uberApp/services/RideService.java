package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.DriverRideDTO;
import com.project.uber.uberApp.dto.RideRequestDTO;
import com.project.uber.uberApp.dto.RiderRideDTO;
import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.RideEntity;
import com.project.uber.uberApp.entities.RideRequestEntity;
import com.project.uber.uberApp.entities.RiderEntity;
import com.project.uber.uberApp.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    RideEntity getRideById(Long rideId);

    RideEntity createNewRide(RideRequestEntity rideRequestEntity, DriverEntity driver);

    RideEntity updateRideStatus(RideEntity ride, RideStatus rideStatus);

    Page<RideEntity> getAllRidesOfRider(RiderEntity rider, PageRequest pageRequest);

    Page<RideEntity> getAllRidesOfDriver(DriverEntity driver, PageRequest pageRequest);
}
