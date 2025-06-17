package com.project.taxiGo.taxiGoApp.strategies;

import com.project.taxiGo.taxiGoApp.entities.Driver;
import com.project.taxiGo.taxiGoApp.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
