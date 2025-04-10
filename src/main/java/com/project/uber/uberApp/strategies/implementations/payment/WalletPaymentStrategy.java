package com.project.uber.uberApp.strategies.implementations.payment;

import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.PaymentEntity;
import com.project.uber.uberApp.entities.RiderEntity;
import com.project.uber.uberApp.enums.PaymentStatus;
import com.project.uber.uberApp.enums.TransactionMethod;
import com.project.uber.uberApp.repositories.PaymentRepository;
import com.project.uber.uberApp.services.PaymentService;
import com.project.uber.uberApp.services.WalletService;
import com.project.uber.uberApp.strategies.PaymentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Rider has 232 in wallet, Driver has 500
// Ride cost is 100, commission = 100 * 0.3 (PLATFORM_COMMISSION)
// Rider -> 232-100 = 132
// Driver -> 500 + (100 - (100*PLATFORM_COMMISSION)) = 500 - (100-30) = 570

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepo;

    @Override
    @Transactional
    public void processPayment(PaymentEntity paymentEntity) {
        DriverEntity driver = paymentEntity.getRide().getDriver();
        RiderEntity rider = paymentEntity.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(), paymentEntity.getAmount(), null, paymentEntity.getRide(), TransactionMethod.RIDE);

        double driversCut = paymentEntity.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(), driversCut, null, paymentEntity.getRide(), TransactionMethod.RIDE);

        paymentEntity.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepo.save(paymentEntity);
    }
}
