package com.rideongo.bms_service.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {

	private Long bookingId;
	private Long bikeId;
	private String bikeName;

	private Long userId;
	private LocalDateTime pickupTs;
	private LocalDateTime dropTs;
	private String rentalType;
	private String pickupType;
	private Long pickupLocationId;
	private String pickupLocationAddress;
	private String pickupLocationCity;
	private String deliveryAddress;
	private Double taxAmount;
	private Double discountAmount;
	private Double totalAmount;
	private String bookingStatus;

	// Additional fields for frontend display

	private String bikeImage;

	private LocalDateTime createdAt;

	// Pickup Location details (for STATION pickup)
	private String pickupLocationState;
	private String pickupLocationPincode;
	private String pickupLocationContact;

}
