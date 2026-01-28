package com.rideongo.bms_service.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandRequestDTO {

	@NotBlank(message = "Brand name is required")
	@Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
	private String brandName;
	
	@NotNull(message = "isActive is required")
	private Boolean isActive;
}
