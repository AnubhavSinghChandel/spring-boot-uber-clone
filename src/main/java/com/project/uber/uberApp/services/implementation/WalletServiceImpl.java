package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.entities.Ride;
import com.project.uber.uberApp.entities.User;
import com.project.uber.uberApp.entities.Wallet;
import com.project.uber.uberApp.entities.WalletTransaction;
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
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findWalletByUser(user);
        wallet.setBalance(wallet.getBalance()+amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
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
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findWalletByUser(user);
        wallet.setBalance(wallet.getBalance()-amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
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
    public Wallet findWalletById(Long walletId) {
        return walletRepo.findById(walletId).orElseThrow(()->new ResourceNotFoundException("Wallet with id "+walletId+" does not exist!"));
    }

    @Override
    public Wallet findWalletByUser(User user) {
        return walletRepo.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("Wallet associated with user id: "+user.getId()+" does not exist!"));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepo.save(wallet);
    }
}
