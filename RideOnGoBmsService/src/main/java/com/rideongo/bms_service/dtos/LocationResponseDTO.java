package com.rideongo.bms_service.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponseDTO {

	private Long id;
	private String address;
	private String city;
	private String state;
	private String pincode;
	private String contactNumber;
	private boolean isActive;
}
