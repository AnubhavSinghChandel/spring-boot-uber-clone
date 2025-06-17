package com.project.taxiGo.taxiGoApp.services.implementation;

import com.project.taxiGo.taxiGoApp.entities.RideRequest;
import com.project.taxiGo.taxiGoApp.exceptions.ResourceNotFoundException;
import com.project.taxiGo.taxiGoApp.repositories.RideRequestRepository;
import com.project.taxiGo.taxiGoApp.services.RideRequestService;
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
