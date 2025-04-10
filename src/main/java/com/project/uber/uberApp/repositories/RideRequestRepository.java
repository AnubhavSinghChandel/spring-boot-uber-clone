package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.RideRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequestEntity, Long> {
}
