package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.DriverDTO;
import com.project.uber.uberApp.dto.RiderDTO;
import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.RideEntity;
import com.project.uber.uberApp.entities.RiderEntity;
import org.springframework.stereotype.Service;

public interface RatingService {

    DriverDTO rateDriver(RideEntity ride, Integer rating);

    RiderDTO rateRider(RideEntity ride, Integer rating);

    void createNewRating(RideEntity ride);
}
