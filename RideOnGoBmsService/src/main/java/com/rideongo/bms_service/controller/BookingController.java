package com.rideongo.bms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideongo.bms_service.dtos.BookingRequestDTO;
import com.rideongo.bms_service.dtos.BookingResponseDTO;
import com.rideongo.bms_service.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

	private final BookingService bookingService;

	@PostMapping
	public ResponseEntity<BookingResponseDTO> createBooking(
			@RequestBody @Valid BookingRequestDTO dto) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(bookingService.createBooking(dto));
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingResponseDTO> getBooking(
			@PathVariable Long bookingId) {

		return ResponseEntity.ok(bookingService.getBookingById(bookingId));
	}

	@GetMapping
	public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
		return ResponseEntity.ok(bookingService.getAllBookings());
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<BookingResponseDTO>> getBookingsByUser(
			@PathVariable Long userId) {

		return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
	}

	@DeleteMapping("/{bookingId}")
	public ResponseEntity<String> cancelBooking(
			@PathVariable Long bookingId) {

		bookingService.cancelBooking(bookingId);
		return ResponseEntity.ok("Booking cancelled successfully");
	}
}
