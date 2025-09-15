package com.carwoosh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for adding/updating a Vehicle for a specific User.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    private String make;               // e.g., Toyota
    private String model;              // e.g., Corolla
    private String variant;            // e.g., VX, LX
    private Integer yearOfManufacture; // e.g., 2021
    private String registrationNumber; // License plate
    private String vin;                // Vehicle Identification Number
    private String fuelType;           // Petrol, Diesel, Electric
    private String color;              // e.g., White, Black

    private Long userId;               // ✅ To link this vehicle to a user
}
