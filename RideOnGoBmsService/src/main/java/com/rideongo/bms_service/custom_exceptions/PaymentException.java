package com.rideongo.bms_service.custom_exceptions;

public class PaymentException extends RuntimeException {
	public PaymentException(String message) {
		super(message);
	}
}
