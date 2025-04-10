package com.project.uber.uberApp.strategies;

import com.project.uber.uberApp.entities.RideRequestEntity;
import org.springframework.stereotype.Service;

@Service
public interface RideFareCalculationStrategy {

    // the fare multiplier can be extracted as a different service and strategies can be
    // applied to make it more optimized based on location, timezone, time of day etc.
    // currently its is hard coded to 10
    double RIDE_FARE_MULTIPLIER = 10;

    Double calculateFare(RideRequestEntity rideRequest);
}