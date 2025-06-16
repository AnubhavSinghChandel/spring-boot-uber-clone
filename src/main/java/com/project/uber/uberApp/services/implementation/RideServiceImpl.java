package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.entities.Driver;
import com.project.uber.uberApp.entities.Ride;
import com.project.uber.uberApp.entities.RideRequest;
import com.project.uber.uberApp.entities.Rider;
import com.project.uber.uberApp.enums.RideRequestStatus;
import com.project.uber.uberApp.enums.RideStatus;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.repositories.RideRepository;
import com.project.uber.uberApp.services.NotificationService;
import com.project.uber.uberApp.services.RideRequestService;
import com.project.uber.uberApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepo;
    private final RideRequestService rideRequestService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepo.findById(rideId)
                .orElseThrow(()-> new ResourceNotFoundException("Ride with id "+rideId+" does not exist!"));
    }

    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {

        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        // convert few fields from rideRequest to ride
        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setId(null);

        notificationService.sendOtpToRider(ride, ride.getOtp());

        rideRequestService.update(rideRequest);
        return rideRepo.save(ride);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepo.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepo.findByRider(rider, pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepo.findByDriver(driver, pageRequest);
    }

    private String generateRandomOTP(){
        Random random = new Random();
        int otpInt = random.nextInt(10000); // 0 to 9999
        return String.format("%04d", otpInt);
    }
}
