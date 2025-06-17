package com.project.taxiGo.taxiGoApp.services;

import com.project.taxiGo.taxiGoApp.dto.DriverDTO;
import com.project.taxiGo.taxiGoApp.dto.RiderDTO;
import com.project.taxiGo.taxiGoApp.entities.Ride;

public interface RatingService {

    DriverDTO rateDriver(Ride ride, Integer rating);

    RiderDTO rateRider(Ride ride, Integer rating);

    void createNewRating(Ride ride);
}
