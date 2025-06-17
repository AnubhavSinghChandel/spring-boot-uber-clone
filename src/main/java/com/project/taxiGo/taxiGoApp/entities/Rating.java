package com.project.taxiGo.taxiGoApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_rating_ride", columnList = "ride_id")
        },
        name = "rating"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ride ride;

    private Integer driverRating;
    private Integer riderRating;
}
