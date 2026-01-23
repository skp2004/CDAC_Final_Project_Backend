package com.rideongo.bms_service.custom_exceptions;

public class DuplicateBrandException extends RuntimeException {
	public DuplicateBrandException(String message) {
		super(message);
	}
}
