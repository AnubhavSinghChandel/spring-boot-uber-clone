package com.project.taxiGo.taxiGoApp.strategies.implementations.rideFare;

import com.project.taxiGo.taxiGoApp.entities.RideRequest;
import com.project.taxiGo.taxiGoApp.services.DistanceService;
import com.project.taxiGo.taxiGoApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public Double calculateFare(RideRequest rideRequest) {

        double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(), rideRequest.getDropOffLocation());

        return distance*RIDE_FARE_MULTIPLIER;
    }
}
