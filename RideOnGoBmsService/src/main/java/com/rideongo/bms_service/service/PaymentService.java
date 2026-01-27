package com.rideongo.bms_service.service;

import java.util.List;

import com.rideongo.bms_service.dtos.PaymentRequestDTO;
import com.rideongo.bms_service.dtos.PaymentResponseDTO;

public interface PaymentService {

	PaymentResponseDTO makePayment(PaymentRequestDTO dto);

	List<PaymentResponseDTO> getPaymentsByBooking(Long bookingId);

	void softDeletePayment(Long paymentId);
}
