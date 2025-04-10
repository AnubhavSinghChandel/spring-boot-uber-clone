package com.project.uber.uberApp.strategies.implementations.payment;

import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.PaymentEntity;
import com.project.uber.uberApp.entities.WalletEntity;
import com.project.uber.uberApp.enums.PaymentStatus;
import com.project.uber.uberApp.enums.TransactionMethod;
import com.project.uber.uberApp.repositories.PaymentRepository;
import com.project.uber.uberApp.services.PaymentService;
import com.project.uber.uberApp.services.WalletService;
import com.project.uber.uberApp.strategies.PaymentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Rider -> 100
// Driver -> 70 (deduct 30 from drivers wallet as commission)

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepo;

    @Override
    @Transactional
    public void processPayment(PaymentEntity paymentEntity) {
        DriverEntity driver = paymentEntity.getRide().getDriver();

        double platformCommission = paymentEntity.getAmount()*PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null, paymentEntity.getRide(), TransactionMethod.RIDE);

        paymentEntity.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepo.save(paymentEntity);
    }
}
