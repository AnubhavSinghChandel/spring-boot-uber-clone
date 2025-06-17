package com.project.taxiGo.taxiGoApp.repositories;

import com.project.taxiGo.taxiGoApp.entities.Rating;
import com.project.taxiGo.taxiGoApp.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRide(Ride ride);
}
