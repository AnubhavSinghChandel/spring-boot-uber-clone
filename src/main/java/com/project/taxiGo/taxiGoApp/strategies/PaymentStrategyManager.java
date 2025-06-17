package com.project.taxiGo.taxiGoApp.strategies;

import com.project.taxiGo.taxiGoApp.enums.PaymentMethod;
import com.project.taxiGo.taxiGoApp.strategies.implementations.payment.CashPaymentStrategy;
import com.project.taxiGo.taxiGoApp.strategies.implementations.payment.WalletPaymentStrategy;
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
