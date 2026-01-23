package com.rideongo.bms_service.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingResponseDTO {

	private Long bookingId;
	private Long bikeId;
	private Long userId;
	private LocalDateTime pickupTs;
	private LocalDateTime dropTs;
	private String rentalType;
	private Double taxAmount;
	private Double discountAmount;
	private Double totalAmount;
	private String bookingStatus;
}
