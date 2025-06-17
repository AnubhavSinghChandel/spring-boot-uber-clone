package com.project.taxiGo.taxiGoApp.repositories;

import com.project.taxiGo.taxiGoApp.entities.Driver;
import com.project.taxiGo.taxiGoApp.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    // ST_Distance(point1, point2)
    // ST_DWithin(point1, 10000)

    @Query(
            value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS distance " +
            "FROM driver_entity d "+
            "WHERE d.available = true AND ST_DWITHIN(d.current_location, :pickupLocation, 10000) "+
            "ORDER BY distance "+
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickupLocation);

    @Query(
            value = "SELECT d.* " +
                    "FROM driver_entity d "+
                    "WHERE d.available = true AND ST_DWITHIN(d.current_location, :pickupLocation, 10000) "+
                    "ORDER BY d.rating DESC "+
                    "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

    Optional<Driver> findByUser(User user);
}
