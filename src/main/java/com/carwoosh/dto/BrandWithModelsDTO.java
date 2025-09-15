package com.carwoosh.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandWithModelsDTO {
    private Long id;
    private String name;
    private List<ModelDTO> models;
}