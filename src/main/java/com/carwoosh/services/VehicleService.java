package com.carwoosh.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carwoosh.base.BaseResponse;
import com.carwoosh.constant.APPServiceCode;
import com.carwoosh.dto.BrandWithModelsDTO;
import com.carwoosh.dto.ModelDTO;
import com.carwoosh.dto.VehicleDTO;
import com.carwoosh.dto.VehicleMetaResponse;
import com.carwoosh.dto.YearDTO;
import com.carwoosh.entity.User;
import com.carwoosh.entity.Vehicle;
import com.carwoosh.repository.UserRepository;
import com.carwoosh.repository.VehicleBrandRepo;
import com.carwoosh.repository.VehicleModelRepo;
import com.carwoosh.repository.VehicleRepository;
import com.carwoosh.repository.VehicleYearRepo;

@Service
public class VehicleService {

	@Autowired
	VehicleBrandRepo brandRepo;

	@Autowired
	VehicleModelRepo modelRepo;

	@Autowired
	VehicleYearRepo yearRepo;

	@Autowired
	UserRepository userRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	public VehicleMetaResponse getAllVehicles() {
		BaseResponse<VehicleMetaResponse> response = new BaseResponse<>();
		List<BrandWithModelsDTO> brands = brandRepo.findAll().stream().map(brand -> {
			List<ModelDTO> models = modelRepo.findByBrandId(brand.getId()).stream()
					.map(m -> new ModelDTO(m.getId(), m.getName())).collect(Collectors.toList());

			return new BrandWithModelsDTO(brand.getId(), brand.getName(), models);
		}).collect(Collectors.toList());

		List<YearDTO> years = yearRepo.findAll().stream().map(y -> new YearDTO(y.getId(), y.getYear()))
				.collect(Collectors.toList());

		return new VehicleMetaResponse(brands, years);
	}

	public APPServiceCode addVehicle(VehicleDTO dto) {
		APPServiceCode serviceCode = null;
		try {
			User user = userRepository.findById(dto.getUserId())
					.orElseThrow(() -> new RuntimeException("User not found"));

			Vehicle vehicle = Vehicle.builder().make(dto.getMake()).model(dto.getModel()).variant(dto.getVariant())
					.yearOfManufacture(dto.getYearOfManufacture()).registrationNumber(dto.getRegistrationNumber())
					.vin(dto.getVin()).fuelType(dto.getFuelType()).color(dto.getColor()).user(user).build();

			vehicleRepository.save(vehicle);
			serviceCode = APPServiceCode.APP_001;
		} catch (Exception e) {
			serviceCode = APPServiceCode.APP_999;
		}
		return serviceCode;
	}

}
