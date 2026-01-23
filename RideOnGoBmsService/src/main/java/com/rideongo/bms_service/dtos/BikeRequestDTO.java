package com.rideongo.bms_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BikeRequestDTO {

	@NotNull(message = "Brand id is required")
	private Long brandId;

	@NotNull
	@Min(50)
	private Long cc;

	@NotNull
	@Size(min = 3, max = 30)
	private String colour;

	@NotNull
	@Min(10)
	private Long mileage;

	@NotNull
	@Min(1)
	private Double ratePerHour;

	@NotNull
	@Min(1)
	private Double ratePerDay;

	@NotNull
	private String fuelType;

	@NotNull
	private String status;
}
