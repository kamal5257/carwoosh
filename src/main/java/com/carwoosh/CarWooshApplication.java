package com.carwoosh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.carwoosh.entity")
public class CarWooshApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarWooshApplication.class, args);
	}

}
