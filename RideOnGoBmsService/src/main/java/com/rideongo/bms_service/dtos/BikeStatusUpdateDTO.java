package com.rideongo.bms_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BikeStatusUpdateDTO {

	@NotNull(message="Status is required.")
	private String status;
}
