package com.rideongo.bms_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BikeResponseDTO {

	public BikeResponseDTO(
	        Long bikeId2,
	        String brandName2,
	        String locationCity2,
	        String locationState2,
	        Long cc2,
	        String colour2,
	        Long mileage2,
	        Double ratePerHour2,
	        Double ratePerDay2,
	        String fuelType2,
	        String status2,
	        String image2
	) {
	    this.bikeId = bikeId2;
	    this.brandName = brandName2;
	    this.locationCity = locationCity2;
	    this.locationState = locationState2;
	    this.cc = cc2;
	    this.colour = colour2;
	    this.mileage = mileage2;
	    this.ratePerHour = ratePerHour2;
	    this.ratePerDay = ratePerDay2;
	    this.fuelType = fuelType2;
	    this.status = status2;
	    this.image = image2;
	}

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
	private String description;
    private String category;  
    private Double pricePer7Days;
}
