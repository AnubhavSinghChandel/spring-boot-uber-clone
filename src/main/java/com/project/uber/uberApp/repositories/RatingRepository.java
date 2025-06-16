package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.Rating;
import com.project.uber.uberApp.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRide(Ride ride);
}
