package com.rideongo.bms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideongo.bms_service.dtos.PaymentRequestDTO;
import com.rideongo.bms_service.dtos.PaymentResponseDTO;
import com.rideongo.bms_service.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<PaymentResponseDTO> makePayment(
			@RequestBody @Valid PaymentRequestDTO dto) {

		return new ResponseEntity<>(
				paymentService.makePayment(dto),
				HttpStatus.CREATED);
	}

	@PostMapping("/create-order")
	public ResponseEntity<com.rideongo.bms_service.dtos.OrderResponseDTO> createOrder(
			@RequestBody com.rideongo.bms_service.dtos.OrderRequestDTO dto) {
		return ResponseEntity.ok(paymentService.createOrder(dto));
	}

	@PostMapping("/verify")
	public ResponseEntity<PaymentResponseDTO> verifyPayment(
			@RequestBody com.rideongo.bms_service.dtos.PaymentVerifyDTO dto) {
		return ResponseEntity.ok(paymentService.verifyPayment(dto));
	}

	@GetMapping("/booking/{bookingId}")
	public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByBooking(
			@PathVariable Long bookingId) {

		return ResponseEntity.ok(paymentService.getPaymentsByBooking(bookingId));
	}

	@GetMapping
	public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
		return ResponseEntity.ok(paymentService.getAllPayments());
	}

	@DeleteMapping("/{paymentId}")
	public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {

		paymentService.softDeletePayment(paymentId);
		return ResponseEntity.ok("Payment deleted successfully");
	}
}
