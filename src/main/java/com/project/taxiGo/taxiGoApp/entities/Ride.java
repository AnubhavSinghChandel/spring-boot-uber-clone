package com.project.taxiGo.taxiGoApp.entities;

import com.project.taxiGo.taxiGoApp.enums.PaymentMethod;
import com.project.taxiGo.taxiGoApp.enums.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
            @Index(name = "idx_ride_rider", columnList = "rider_id"),
            @Index(name = "idx_ride_driver", columnList = "driver_id")
        },
        name = "ride"
)
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickupLocation;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

    private String otp;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Double fare;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
