package com.carwoosh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMetaResponse {
	private List<BrandWithModelsDTO> brands;
    private List<YearDTO> years;
}
