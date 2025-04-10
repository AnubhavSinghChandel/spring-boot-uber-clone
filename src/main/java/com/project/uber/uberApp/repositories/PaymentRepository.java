package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.PaymentEntity;
import com.project.uber.uberApp.entities.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByRide(RideEntity ride);
}
