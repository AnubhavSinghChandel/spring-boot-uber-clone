package com.project.uber.uberApp.entities;

import com.project.uber.uberApp.enums.PaymentMethod;
import com.project.uber.uberApp.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne(fetch = FetchType.LAZY)
    private RideEntity ride;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    private LocalDateTime paymentTime;
}