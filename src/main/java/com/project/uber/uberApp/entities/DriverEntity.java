package com.project.uber.uberApp.entities;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_driverEntity_vehicleId", columnList = "vehicleId")
        },
        name = "driver"
)
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    private Double rating;

    private Boolean available;

    private String vehicleId;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;
}
