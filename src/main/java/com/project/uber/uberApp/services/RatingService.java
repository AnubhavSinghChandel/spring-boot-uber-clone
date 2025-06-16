package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.DriverDTO;
import com.project.uber.uberApp.dto.RiderDTO;
import com.project.uber.uberApp.entities.Ride;

public interface RatingService {

    DriverDTO rateDriver(Ride ride, Integer rating);

    RiderDTO rateRider(Ride ride, Integer rating);

    void createNewRating(Ride ride);
}
