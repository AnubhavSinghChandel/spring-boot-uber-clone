package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.WalletTransactionDTO;
import com.project.uber.uberApp.entities.WalletTransactionEntity;
import org.springframework.stereotype.Service;

public interface WalletTransactionService {

    void createNewWalletTransaction(WalletTransactionEntity walletTransaction);
}
