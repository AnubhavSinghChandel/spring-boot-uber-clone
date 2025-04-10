package com.project.uber.uberApp.entities;

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
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private RideEntity ride;

    private Integer driverRating;
    private Integer riderRating;
}
