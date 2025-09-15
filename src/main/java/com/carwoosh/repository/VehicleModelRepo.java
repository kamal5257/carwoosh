package com.carwoosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carwoosh.entity.VehicleModel;

@Repository
public interface VehicleModelRepo extends JpaRepository<VehicleModel, Long> {
	List<VehicleModel> findByBrandId(Long brandId);
}
