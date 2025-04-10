package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.dto.RideDTO;
import com.project.uber.uberApp.dto.WalletDTO;
import com.project.uber.uberApp.dto.WalletTransactionDTO;
import com.project.uber.uberApp.entities.RideEntity;
import com.project.uber.uberApp.entities.UserEntity;
import com.project.uber.uberApp.entities.WalletEntity;
import com.project.uber.uberApp.entities.WalletTransactionEntity;
import com.project.uber.uberApp.enums.TransactionMethod;
import com.project.uber.uberApp.enums.TransactionType;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.repositories.WalletRepository;
import com.project.uber.uberApp.services.WalletService;
import com.project.uber.uberApp.services.WalletTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;
    private final ModelMapper modelMapper;
    private final WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public WalletEntity addMoneyToWallet(UserEntity user, Double amount, String transactionId, RideEntity ride, TransactionMethod transactionMethod) {
        WalletEntity wallet = findWalletByUser(user);
        wallet.setBalance(wallet.getBalance()+amount);

        WalletTransactionEntity walletTransaction = WalletTransactionEntity.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepo.save(wallet);
    }

    @Override
    public void withdrawFromWallet() {

    }

    @Override
    @Transactional
    public WalletEntity deductMoneyFromWallet(UserEntity user, Double amount, String transactionId, RideEntity ride, TransactionMethod transactionMethod) {
        WalletEntity wallet = findWalletByUser(user);
        wallet.setBalance(wallet.getBalance()-amount);

        WalletTransactionEntity walletTransaction = WalletTransactionEntity.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepo.save(wallet);
    }

    @Override
    public WalletEntity findWalletById(Long walletId) {
        return walletRepo.findById(walletId).orElseThrow(()->new ResourceNotFoundException("Wallet with id "+walletId+" does not exist!"));
    }

    @Override
    public WalletEntity findWalletByUser(UserEntity user) {
        return walletRepo.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("Wallet associated with user id: "+user.getId()+" does not exist!"));
    }

    @Override
    public WalletEntity createNewWallet(UserEntity user) {
        WalletEntity wallet = new WalletEntity();
        wallet.setUser(user);
        return walletRepo.save(wallet);
    }
}
