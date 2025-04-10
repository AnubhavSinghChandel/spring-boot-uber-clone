package com.project.uber.uberApp.strategies;

import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.RideRequestEntity;

import java.util.List;

public interface DriverMatchingStrategy {

    List<DriverEntity> findMatchingDriver(RideRequestEntity rideRequest);
}
