package com.project.taxiGo.taxiGoApp.services;

import com.project.taxiGo.taxiGoApp.entities.Ride;
import com.project.taxiGo.taxiGoApp.entities.User;
import com.project.taxiGo.taxiGoApp.entities.Wallet;
import com.project.taxiGo.taxiGoApp.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    void withdrawFromWallet();

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet findWalletById(Long walletId);

    Wallet findWalletByUser(User user);

    Wallet createNewWallet(User user);
}
