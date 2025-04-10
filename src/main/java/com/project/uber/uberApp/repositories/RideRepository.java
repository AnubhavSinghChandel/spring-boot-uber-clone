package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.RideEntity;
import com.project.uber.uberApp.entities.RiderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<RideEntity, Long> {
    Page<RideEntity> findByRider(RiderEntity rider, PageRequest pageRequest);

    Page<RideEntity> findByDriver(DriverEntity driver, PageRequest pageRequest);
}
