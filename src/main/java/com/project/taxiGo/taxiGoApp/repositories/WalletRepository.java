package com.project.taxiGo.taxiGoApp.repositories;

import com.project.taxiGo.taxiGoApp.entities.User;
import com.project.taxiGo.taxiGoApp.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
}
