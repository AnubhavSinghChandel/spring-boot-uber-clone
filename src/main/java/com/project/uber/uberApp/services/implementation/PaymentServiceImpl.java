package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.entities.PaymentEntity;
import com.project.uber.uberApp.entities.RideEntity;
import com.project.uber.uberApp.enums.PaymentStatus;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.repositories.PaymentRepository;
import com.project.uber.uberApp.services.PaymentService;
import com.project.uber.uberApp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(RideEntity ride) {
        PaymentEntity payment = paymentRepository.findByRide(ride)
                .orElseThrow(()->new ResourceNotFoundException("Payment associated to ride with id: "+ride.getId()+" does not exist"));
        paymentStrategyManager.paymentStrategy(ride.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public PaymentEntity createNewPayment(RideEntity ride) {
        PaymentEntity payment = PaymentEntity.builder()
                .paymentMethod(ride.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .amount(ride.getFare())
                .ride(ride)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(PaymentEntity payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }
}
