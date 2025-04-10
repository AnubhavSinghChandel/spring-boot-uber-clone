package com.project.uber.uberApp.services;

import com.project.uber.uberApp.entities.PaymentEntity;
import com.project.uber.uberApp.entities.RideEntity;
import com.project.uber.uberApp.enums.PaymentStatus;
import org.springframework.stereotype.Service;

public interface PaymentService {

    void processPayment(RideEntity ride);

    PaymentEntity createNewPayment(RideEntity ride);

    void updatePaymentStatus(PaymentEntity payment, PaymentStatus paymentStatus);
}
