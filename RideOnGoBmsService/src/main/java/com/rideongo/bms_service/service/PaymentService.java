package com.rideongo.bms_service.service;

import java.util.List;

import com.rideongo.bms_service.dtos.PaymentRequestDTO;
import com.rideongo.bms_service.dtos.PaymentResponseDTO;
import com.rideongo.bms_service.dtos.RazorpayOrderRequest;
import com.rideongo.bms_service.dtos.RazorpayOrderResponse;
import com.rideongo.bms_service.dtos.RazorpayPaymentVerification;

public interface PaymentService {

	PaymentResponseDTO makePayment(PaymentRequestDTO dto);

	// Razorpay specific
	com.rideongo.bms_service.dtos.OrderResponseDTO createOrder(com.rideongo.bms_service.dtos.OrderRequestDTO dto);

	PaymentResponseDTO verifyPayment(com.rideongo.bms_service.dtos.PaymentVerifyDTO dto);

	List<PaymentResponseDTO> getPaymentsByBooking(Long bookingId);

	List<PaymentResponseDTO> getAllPayments();

	void softDeletePayment(Long paymentId);

	RazorpayOrderResponse createRazorpayOrder(RazorpayOrderRequest request);

	PaymentResponseDTO verifyAndSavePayment(RazorpayPaymentVerification request);
}
