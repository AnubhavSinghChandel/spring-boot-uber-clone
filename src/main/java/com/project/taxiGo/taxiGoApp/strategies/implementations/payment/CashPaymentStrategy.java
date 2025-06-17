package com.project.taxiGo.taxiGoApp.strategies.implementations.payment;

import com.project.taxiGo.taxiGoApp.entities.Driver;
import com.project.taxiGo.taxiGoApp.entities.Payment;
import com.project.taxiGo.taxiGoApp.enums.PaymentStatus;
import com.project.taxiGo.taxiGoApp.enums.TransactionMethod;
import com.project.taxiGo.taxiGoApp.repositories.PaymentRepository;
import com.project.taxiGo.taxiGoApp.services.WalletService;
import com.project.taxiGo.taxiGoApp.strategies.PaymentStrategy;
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
