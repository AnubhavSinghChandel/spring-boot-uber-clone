package com.project.uber.uberApp.dto;

import com.project.uber.uberApp.enums.PaymentMethod;
import com.project.uber.uberApp.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDTO {

    private Long id;
    private PointDTO pickupLocation;
    private PointDTO dropOffLocation;
    private RiderDTO rider;
    private Double fare;
    private Double distance;
    private PaymentMethod paymentMethod;
    private RideRequestStatus rideRequestStatus;
    private LocalDateTime requestedTime;
}
