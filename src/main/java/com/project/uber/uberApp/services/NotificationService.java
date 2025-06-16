package com.project.uber.uberApp.services;

import com.project.uber.uberApp.entities.Ride;

public interface NotificationService {

    void sendOtpToRider(Ride ride, String otp);
}
