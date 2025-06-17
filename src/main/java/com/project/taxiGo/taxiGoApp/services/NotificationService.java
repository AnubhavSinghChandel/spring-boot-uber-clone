package com.project.taxiGo.taxiGoApp.services;

import com.project.taxiGo.taxiGoApp.entities.Ride;

public interface NotificationService {

    void sendOtpToRider(Ride ride, String otp);
}
