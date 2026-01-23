package com.rideongo.bms_service.custom_exceptions;

public class BookingNotFoundException extends RuntimeException {
	public BookingNotFoundException(String message) {
		super(message);
	}
}
