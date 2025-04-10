package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.UserEntity;
import com.project.uber.uberApp.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findByUser(UserEntity user);
}
