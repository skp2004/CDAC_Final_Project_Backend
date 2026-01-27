package com.rideongo.bms_service.dtos;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {

	private Long id;
	private Long bookingId;
	private Double amount;
	private String paymentMode;
	private String paymentType;
	private String txnRef;
	private String status;
	private LocalDateTime createdAt;
}
