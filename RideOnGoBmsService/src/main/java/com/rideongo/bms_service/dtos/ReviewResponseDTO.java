package com.rideongo.bms_service.dtos;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDTO {

	private Long id;
	private Integer rating;
	private String comments;
	private Long bikeId;
	private Long userId;
	private LocalDateTime createdAt;
}
