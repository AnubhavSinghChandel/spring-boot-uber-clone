package com.project.uber.uberApp.strategies;

import com.project.uber.uberApp.enums.PaymentMethod;
import com.project.uber.uberApp.strategies.implementations.payment.CashPaymentStrategy;
import com.project.uber.uberApp.strategies.implementations.payment.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
        if(paymentMethod.equals(PaymentMethod.CASH)){
            return cashPaymentStrategy;
        }else
            return walletPaymentStrategy;
    }

}
