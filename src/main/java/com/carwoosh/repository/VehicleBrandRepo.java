package com.carwoosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carwoosh.entity.VehicleBrand;

@Repository
public interface VehicleBrandRepo extends JpaRepository<VehicleBrand, Long> {
	
	

}
