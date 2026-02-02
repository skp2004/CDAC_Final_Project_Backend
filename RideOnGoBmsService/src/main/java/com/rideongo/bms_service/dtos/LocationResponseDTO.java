package com.rideongo.bms_service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
	@JsonProperty("isActive")
	private boolean isActive;
	private Long totalBikes;

}
