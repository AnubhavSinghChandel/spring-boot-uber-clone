package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.RatingEntity;
import com.project.uber.uberApp.entities.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    Optional<RatingEntity> findByRide(RideEntity ride);
}
