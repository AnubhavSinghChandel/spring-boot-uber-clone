package com.project.taxiGo.taxiGoApp.services.implementation;

import com.project.taxiGo.taxiGoApp.entities.Payment;
import com.project.taxiGo.taxiGoApp.entities.Ride;
import com.project.taxiGo.taxiGoApp.enums.PaymentStatus;
import com.project.taxiGo.taxiGoApp.exceptions.ResourceNotFoundException;
import com.project.taxiGo.taxiGoApp.repositories.PaymentRepository;
import com.project.taxiGo.taxiGoApp.services.PaymentService;
import com.project.taxiGo.taxiGoApp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(()->new ResourceNotFoundException("Payment associated to ride with id: "+ride.getId()+" does not exist"));
        paymentStrategyManager.paymentStrategy(ride.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .paymentMethod(ride.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .amount(ride.getFare())
                .ride(ride)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }
}
