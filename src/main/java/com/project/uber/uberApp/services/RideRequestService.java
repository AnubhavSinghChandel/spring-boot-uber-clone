package com.project.uber.uberApp.services;

import com.project.uber.uberApp.entities.RideRequestEntity;
import org.springframework.stereotype.Service;

public interface RideRequestService {

    RideRequestEntity findRideRequestById(Long rideRequestId);

    void update(RideRequestEntity rideRequestEntity);
}
