package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.dto.WalletTransactionDTO;
import com.project.uber.uberApp.entities.WalletTransactionEntity;
import com.project.uber.uberApp.repositories.WalletTransactionRepository;
import com.project.uber.uberApp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepo;
    private final ModelMapper modelMapper;

    @Override
    public void createNewWalletTransaction(WalletTransactionEntity walletTransaction) {
        walletTransactionRepo.save(walletTransaction);
    }
}
