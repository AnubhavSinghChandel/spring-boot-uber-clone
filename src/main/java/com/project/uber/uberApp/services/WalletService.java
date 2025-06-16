package com.project.uber.uberApp.services;

import com.project.uber.uberApp.entities.Ride;
import com.project.uber.uberApp.entities.User;
import com.project.uber.uberApp.entities.Wallet;
import com.project.uber.uberApp.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    void withdrawFromWallet();

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet findWalletById(Long walletId);

    Wallet findWalletByUser(User user);

    Wallet createNewWallet(User user);
}
