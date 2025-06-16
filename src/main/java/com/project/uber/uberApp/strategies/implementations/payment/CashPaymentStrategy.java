package com.project.uber.uberApp.strategies.implementations.payment;

import com.project.uber.uberApp.entities.Driver;
import com.project.uber.uberApp.entities.Payment;
import com.project.uber.uberApp.enums.PaymentStatus;
import com.project.uber.uberApp.enums.TransactionMethod;
import com.project.uber.uberApp.repositories.PaymentRepository;
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
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount()*PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null, payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepo.save(payment);
    }
}
