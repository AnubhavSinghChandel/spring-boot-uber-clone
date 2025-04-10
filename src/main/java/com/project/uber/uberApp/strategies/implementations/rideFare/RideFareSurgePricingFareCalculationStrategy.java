package com.project.uber.uberApp.strategies.implementations.rideFare;

import com.project.uber.uberApp.entities.RideRequestEntity;
import com.project.uber.uberApp.services.DistanceService;
import com.project.uber.uberApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;
    // In real world application surge factor can depend
    // on many other things for example weather, day of week etc.
    private static final double SURGE_FACTOR = 2;

    @Override
    public Double calculateFare(RideRequestEntity rideRequest) {

        double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(), rideRequest.getDropOffLocation());

        return distance*RIDE_FARE_MULTIPLIER*SURGE_FACTOR;
    }
}
