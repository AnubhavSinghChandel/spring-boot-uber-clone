package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.entities.RideRequest;
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
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepo.findById(rideRequestId).orElseThrow(() -> new ResourceNotFoundException("Ride Request with id "+rideRequestId+" not found"));
    }

    @Override
    public void update(RideRequest rideRequest) {
        RideRequest toSave = rideRequestRepo.findById(rideRequest.getId())
                .orElseThrow(()->new ResourceNotFoundException("Ride Request with id "+ rideRequest.getId()+" does not exists!"));
        rideRequestRepo.save(toSave);
    }
}
