package com.rideongo.bms_service.custom_exceptions;

public class BikeNotFoundException extends RuntimeException {
	public BikeNotFoundException(String message) {
		super(message);
	}
}
