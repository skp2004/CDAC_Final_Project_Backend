package com.rideongo.bms_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponseDTO {

	private Long brandId;
	private String brandName;
	private Boolean isActive; 
	private Long totalBikes;
}
