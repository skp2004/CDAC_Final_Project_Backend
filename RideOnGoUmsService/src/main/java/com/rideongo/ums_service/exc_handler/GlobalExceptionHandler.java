package com.rideongo.ums_service.exc_handler;



import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.rideongo.ums_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.ums_service.custom_exceptions.UnauthorizedException;
import com.rideongo.ums_service.dtos.ApiResponse;

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
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ApiResponse> handleIOException(IOException e) {
		log.error("IO Exception occurred: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("ERROR", "Failed to process file upload: " + e.getMessage()));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiResponse> handleMaxUploadSizeExceededException(
			MaxUploadSizeExceededException e) {
		log.error("File size exceeded: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
				.body(new ApiResponse("ERROR", "File size exceeds maximum limit of 10MB"));
	}
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
		System.out.println("in catch -Spring sec detected  Authentication Exception "+e);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Failed", e.getMessage()));
	}
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
	    log.error("Unauthorized access attempt: {}", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.FORBIDDEN)
	            .body(new ApiResponse("Failed", ex.getMessage()));
	}
}
