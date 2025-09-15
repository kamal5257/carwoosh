package com.carwoosh.controller;



import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carwoosh.base.BaseResponse;
import com.carwoosh.constant.APPServiceCode;
import com.carwoosh.dto.VehicleDTO;
import com.carwoosh.dto.VehicleMetaResponse;
import com.carwoosh.services.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private static final String ADD_VEHICLE = "/add";
//    private static final String DELETE_VEHICLE = "/delete/{id}";
//    private static final String GET_VEHICLE = "/{id}";
    private static final String GET_ALL_VEHICLES = "/all";
    

    @Autowired
    private VehicleService vehicleService;

//     Add Vehicle
    @PostMapping(ADD_VEHICLE)
    public BaseResponse<Object> addVehicle(@RequestBody @Valid VehicleDTO vehicleDTO, BindingResult bindingResult) {
        BaseResponse<Object> response = new BaseResponse<>();
        APPServiceCode code;
        try {
            if (bindingResult.hasErrors()) {
                FieldError error = bindingResult.getFieldErrors().get(0);
//                log.error("Validation error: {}", error.getDefaultMessage());
                code = APPServiceCode.valueOf(error.getDefaultMessage());
                response.setStatusCode(code.getStatusCode());
                response.setMessage(code.getStatusDesc());
            } else {
                code = vehicleService.addVehicle(vehicleDTO);
            }
        } catch (Exception e) {
//            log.error("Error while adding vehicle", e);
            code = APPServiceCode.APP_999;
            response.setStatusCode(code.getStatusCode());
            response.setMessage(code.getStatusDesc());
        }
        return response;
    }

//    // Delete Vehicle
//    @DeleteMapping(DELETE_VEHICLE)
//    public BaseResponse<Object> deleteVehicle(@PathVariable Long id) {
//        return vehicleService.deleteVehicle(id);
//    }

    // Get Vehicle by ID
//    @GetMapping(GET_VEHICLE)
//    public BaseResponse<Object> getVehicle(@PathVariable Long id) {
//        return vehicleService.getVehicleById(id);
//    }

    
    // Get All Vehicles
    @RequestMapping(value = GET_ALL_VEHICLES, method = RequestMethod.POST)
    public BaseResponse<VehicleMetaResponse> getVehicleMeta() {
        BaseResponse<VehicleMetaResponse> response = new BaseResponse<>();
        try {
            VehicleMetaResponse metaResponse = vehicleService.getAllVehicles();
            
            response.setStatusCode(APPServiceCode.APP_001.getStatusCode());
            response.setMessage(APPServiceCode.APP_001.getStatusDesc());
            
            Map<String, Object> data = new HashMap<>();
            data.put("brands", metaResponse.getBrands());
            data.put("years", metaResponse.getYears());
            response.setData(data);
        } catch (Exception e) {
//            log.error("Error while fetching vehicle metadata", e);
            response.setStatusCode(APPServiceCode.APP_999.getStatusCode());
            response.setMessage(APPServiceCode.APP_999.getStatusDesc());
        }
        return response;
    }
}
