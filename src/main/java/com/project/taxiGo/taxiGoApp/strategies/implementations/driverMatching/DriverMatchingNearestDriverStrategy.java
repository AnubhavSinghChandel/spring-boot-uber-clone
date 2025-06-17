package com.project.taxiGo.taxiGoApp.strategies.implementations.driverMatching;

import com.project.taxiGo.taxiGoApp.entities.Driver;
import com.project.taxiGo.taxiGoApp.entities.RideRequest;
import com.project.taxiGo.taxiGoApp.repositories.DriverRepository;
import com.project.taxiGo.taxiGoApp.strategies.DriverMatchingStrategy;
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
