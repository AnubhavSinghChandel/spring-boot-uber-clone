package com.project.uber.uberApp.services;

import com.project.uber.uberApp.entities.RideEntity;
import com.project.uber.uberApp.entities.UserEntity;
import com.project.uber.uberApp.entities.WalletEntity;
import com.project.uber.uberApp.enums.TransactionMethod;
import com.project.uber.uberApp.enums.TransactionType;
import org.springframework.stereotype.Service;

public interface WalletService {

    WalletEntity addMoneyToWallet(UserEntity user, Double amount, String transactionId, RideEntity ride, TransactionMethod transactionMethod);

    void withdrawFromWallet();

    WalletEntity deductMoneyFromWallet(UserEntity user, Double amount, String transactionId, RideEntity ride, TransactionMethod transactionMethod);

    WalletEntity findWalletById(Long walletId);

    WalletEntity findWalletByUser(UserEntity user);

    WalletEntity createNewWallet(UserEntity user);
}
