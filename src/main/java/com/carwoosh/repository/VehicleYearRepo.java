package com.carwoosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carwoosh.entity.VehicleYear;

@Repository
public interface VehicleYearRepo extends JpaRepository<VehicleYear, Long>{

}
