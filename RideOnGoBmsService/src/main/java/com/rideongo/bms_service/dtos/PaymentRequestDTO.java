package com.rideongo.bms_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

	@NotNull(message = "Booking id is required")
	private Long bookingId;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	private Double amount;

	@NotBlank(message = "Payment mode is required")
	private String paymentMode;

	@NotBlank(message = "Payment type is required")
	private String paymentType;

	@NotBlank(message = "Transaction reference is required")
	private String txnRef;
}
