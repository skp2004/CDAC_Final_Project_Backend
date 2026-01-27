package com.rideongo.bms_service.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {

	@NotNull(message = "Rating is required")
	@Min(value = 1, message = "Rating must be at least 1")
	@Max(value = 5, message = "Rating cannot exceed 5")
	private Integer rating;

	@Size(max = 500, message = "Comments cannot exceed 500 characters")
	private String comments;

	@NotNull(message = "Bike id is required")
	private Long bikeId;
}
