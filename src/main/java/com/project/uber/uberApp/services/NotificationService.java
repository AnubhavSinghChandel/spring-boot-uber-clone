package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.RiderRideDTO;
import com.project.uber.uberApp.entities.RideEntity;
import org.springframework.stereotype.Service;

public interface NotificationService {

    void sendOtpToRider(RideEntity ride, String otp);
}
