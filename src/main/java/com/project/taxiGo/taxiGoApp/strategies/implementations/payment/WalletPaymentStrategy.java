package com.project.taxiGo.taxiGoApp.strategies.implementations.payment;

import com.project.taxiGo.taxiGoApp.entities.Driver;
import com.project.taxiGo.taxiGoApp.entities.Payment;
import com.project.taxiGo.taxiGoApp.entities.Rider;
import com.project.taxiGo.taxiGoApp.enums.PaymentStatus;
import com.project.taxiGo.taxiGoApp.enums.TransactionMethod;
import com.project.taxiGo.taxiGoApp.repositories.PaymentRepository;
import com.project.taxiGo.taxiGoApp.services.WalletService;
import com.project.taxiGo.taxiGoApp.strategies.PaymentStrategy;
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
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(), driversCut, null, payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepo.save(payment);
    }
}
