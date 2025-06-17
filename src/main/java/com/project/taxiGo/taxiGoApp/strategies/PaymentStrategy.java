package com.project.taxiGo.taxiGoApp.strategies;

import com.project.taxiGo.taxiGoApp.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
