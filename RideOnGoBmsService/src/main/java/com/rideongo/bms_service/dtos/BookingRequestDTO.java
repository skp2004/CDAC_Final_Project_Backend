package com.rideongo.bms_service.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestDTO {

	@NotNull
	private Long bikeId;

	@NotNull
	private Long userId;

	@NotNull
	private LocalDateTime pickupTs;

	@NotNull
	private LocalDateTime dropTs;

	@NotNull
	private String rentalType;

	@NotNull
	private String pickupType; // DOORSTEP or STATION

	private Long pickupLocationId; // Required if STATION

	private String deliveryAddress; // Required if DOORSTEP

	@NotNull
	@Min(0)
	private Double taxAmount;

	@NotNull
	@Min(0)
	private Double discountAmount;

	@NotNull
	@Min(1)
	private Double totalAmount;
}
