package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.entities.RideRequestEntity;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.repositories.RideRequestRepository;
import com.project.uber.uberApp.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepo;

    @Override
    public RideRequestEntity findRideRequestById(Long rideRequestId) {
        return rideRequestRepo.findById(rideRequestId).orElseThrow(() -> new ResourceNotFoundException("Ride Request with id "+rideRequestId+" not found"));
    }

    @Override
    public void update(RideRequestEntity rideRequestEntity) {
        RideRequestEntity toSave = rideRequestRepo.findById(rideRequestEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Ride Request with id "+rideRequestEntity.getId()+" does not exists!"));
        rideRequestRepo.save(toSave);
    }
}
