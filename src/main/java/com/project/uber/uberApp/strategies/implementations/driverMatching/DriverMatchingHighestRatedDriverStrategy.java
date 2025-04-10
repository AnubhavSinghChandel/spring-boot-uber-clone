package com.project.uber.uberApp.strategies.implementations.driverMatching;

import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.RideRequestEntity;
import com.project.uber.uberApp.repositories.DriverRepository;
import com.project.uber.uberApp.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepo;

    @Override
    public List<DriverEntity> findMatchingDriver(RideRequestEntity rideRequest) {

        return driverRepo.findTenNearbyTopRatedDrivers(rideRequest.getPickupLocation());
    }
}
