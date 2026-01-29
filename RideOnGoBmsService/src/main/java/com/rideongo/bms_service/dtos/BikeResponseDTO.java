package com.rideongo.bms_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BikeResponseDTO {

	private Long bikeId;
	private String brandName;
	private String locationCity;   
	private String locationState;
	private Long cc;
	private String colour;
	private Long mileage;
	private Double ratePerHour;
	private Double ratePerDay;
	private String fuelType;
	private String status;
	private String image; 
}
