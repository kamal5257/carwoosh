package com.carwoosh.enums;

public class VehicleEnums {

	// ---------------- VEHICLE TYPE ----------------
	public enum VehicleType {
		CAR, BIKE, SCOOTER
	}

	// ---------------- VEHICLE CATEGORY ----------------
	public enum VehicleCategory {
		// Cars
		HATCHBACK, SEDAN, SUV, MPV, // Multi-Purpose Vehicle
		COUPE, CONVERTIBLE,

		// Bikes
		COMMUTER, SPORTS, CRUISER, ADVENTURE,

		// Scooters
		SCOOTER, ELECTRIC
	}

	// ---------------- FUEL TYPE ----------------
	public enum FuelType {
		PETROL, DIESEL, CNG, ELECTRIC, HYBRID
	}

	// ---------------- VEHICLE MAKE (BRANDS) ----------------
	public enum VehicleMake {
		// Car brands
		MARUTI_SUZUKI, TATA, MAHINDRA, HYUNDAI, HONDA, TOYOTA, KIA, MG, SKODA, VOLKSWAGEN, RENAULT, FORD, BMW,

		// Bike brands
		HERO, BAJAJ, TVS, ROYAL_ENFIELD, YAMAHA, SUZUKI,

		// Scooter / EV brands
		OLA, ATHER, HERO_ELECTRIC, SIMPLE_ENERGY, OKAYA
	}

	// ---------------- VEHICLE MODEL (POPULAR EXAMPLES) ----------------
	public enum VehicleModel {
		// Maruti Suzuki
		SWIFT, BALENO, ERTIGA, ALTO,

		// Tata
		NEXON, HARRIER, ALTROZ,

		// Mahindra
		SCORPIO, THAR, BOLERO,

		// Hyundai
		CRETA, VENUE, I20,

		// Kia
		SELTOS, SONET,

		// Honda (Cars)
		CITY, AMAZE,

		// Toyota
		INNOVA_CRYSTA, FORTUNER,

		// Bikes
		HONDA_SHINE, HONDA_HORNET_2_0, HERO_SPLENDOR_PLUS, HERO_XPULSE_200, BAJAJ_PULSAR_150, BAJAJ_DOMINAR_400,
		TVS_APACHE_RTR_160, TVS_RAIDER, ROYAL_ENFIELD_CLASSIC_350, ROYAL_ENFIELD_HUNTER_350, YAMAHA_FZ_V3,
		YAMAHA_R15_V4, SUZUKI_GIXXER_250,

		// Scooters
		HONDA_ACTIVA_6G, HONDA_DIO, TVS_JUPITER, TVS_NTORQ_125, BAJAJ_CHETAK, OLA_S1, ATHER_450X, HERO_OPTIMA_CX,
		SIMPLE_ONE, OKAYA_FAST_F4
	}
}
