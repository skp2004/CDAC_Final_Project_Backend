package com.rideongo.bms_service.exc_handler;



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rideongo.bms_service.custom_exceptions.BikeNotFoundException;
import com.rideongo.bms_service.custom_exceptions.BookingNotFoundException;
import com.rideongo.bms_service.custom_exceptions.DuplicateBrandException;
import com.rideongo.bms_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.bms_service.dtos.ApiResponse;

import lombok.extern.slf4j.Slf4j;




@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		System.out.println("in catch all ");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed", e.getMessage()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
		System.out.println("in catch - ResourceNotFoundException");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed", e.getMessage()));
	}	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		System.out.println("in catch - MethodArgumentNotValidException");
		List<FieldError> list = e.getFieldErrors();
	

		Map<String, String> map = list.stream()				
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,(v1,v2) -> v1+" "+v2));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
	}
	


	
	

	@ExceptionHandler(DuplicateBrandException.class)
	public ResponseEntity<String> handleDuplicate(DuplicateBrandException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	@ExceptionHandler(BikeNotFoundException.class)
	public ResponseEntity<String> handleBikeNotFound(BikeNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<String> handleBookingNotFound(BookingNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}


}
