package com.project.taxiGo.taxiGoApp.services;

import com.project.taxiGo.taxiGoApp.entities.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
