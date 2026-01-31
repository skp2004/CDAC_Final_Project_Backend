package com.rideongo.bms_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CityResponseDTO {

	private Long locationId;
	private String city;
}
