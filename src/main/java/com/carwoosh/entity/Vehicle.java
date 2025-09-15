package com.carwoosh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vehicle info
    @Column(nullable = false)
    private String make;   // e.g., Toyota, Honda

    @Column(nullable = false)
    private String model;  // e.g., Corolla, Civic

    private String variant; // e.g., VX, LX, Diesel, Petrol

    private Integer yearOfManufacture;

    @Column(nullable = false, unique = true)
    private String registrationNumber; // License plate

    private String vin; // Vehicle Identification Number (optional)

    private String fuelType; // Petrol, Diesel, Electric, Hybrid

    private String color;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

