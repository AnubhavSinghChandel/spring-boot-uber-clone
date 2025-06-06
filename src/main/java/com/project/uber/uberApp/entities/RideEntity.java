package com.project.uber.uberApp.entities;

import com.project.uber.uberApp.enums.PaymentMethod;
import com.project.uber.uberApp.enums.RideStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
            @Index(name = "idx_rideEntity_rider", columnList = "rider_id"),
            @Index(name = "idx_rideEntity_driver", columnList = "driver_id")
        },
        name = "ride"
)
public class RideEntity {

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
    private RiderEntity rider;

    @ManyToOne(fetch = FetchType.LAZY)
    private DriverEntity driver;

    private String otp;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Double fare;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
