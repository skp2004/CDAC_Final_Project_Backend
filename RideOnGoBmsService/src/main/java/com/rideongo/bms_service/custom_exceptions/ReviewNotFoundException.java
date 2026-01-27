package com.rideongo.bms_service.custom_exceptions;

public class ReviewNotFoundException extends RuntimeException {
	public ReviewNotFoundException(String message) {
		super(message);
	}
}
