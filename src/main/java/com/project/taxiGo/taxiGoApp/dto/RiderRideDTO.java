package com.project.taxiGo.taxiGoApp.dto;

import com.project.taxiGo.taxiGoApp.enums.PaymentMethod;
import com.project.taxiGo.taxiGoApp.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderRideDTO {

    private Long id;
    private PointDTO pickUpLocation;
    private PointDTO dropOffLocation;
    private RiderDTO rider;
    private DriverDTO driver;
    private LocalDateTime createdTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String otp;
    private RideStatus rideStatus;
    private Double fare;
    private PaymentMethod paymentMethod;
}
