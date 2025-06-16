package com.project.uber.uberApp.strategies.implementations.driverMatching;

import com.project.uber.uberApp.entities.Driver;
import com.project.uber.uberApp.entities.RideRequest;
import com.project.uber.uberApp.repositories.DriverRepository;
import com.project.uber.uberApp.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepo;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {

        return driverRepo.findTenNearestDrivers(rideRequest.getPickupLocation());
    }
}
