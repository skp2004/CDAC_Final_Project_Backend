package com.rideongo.bms_service.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rideongo.bms_service.custom_exceptions.PaymentException;
import com.rideongo.bms_service.dtos.PaymentRequestDTO;
import com.rideongo.bms_service.dtos.PaymentResponseDTO;
import com.rideongo.bms_service.entities.Payment;
import com.rideongo.bms_service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final ModelMapper modelMapper;

	@Override
	public PaymentResponseDTO makePayment(PaymentRequestDTO dto) {

		if (paymentRepository.existsByTxnRef(dto.getTxnRef())) {
			throw new PaymentException("Duplicate transaction reference");
		}

		Payment payment = new Payment();
		payment.setBookingId(dto.getBookingId());
		payment.setAmount(dto.getAmount());
		payment.setPaymentMode(dto.getPaymentMode());
		payment.setPaymentType(dto.getPaymentType());
		payment.setTxnRef(dto.getTxnRef());
		payment.setStatus("SUCCESS");

		return modelMapper.map(paymentRepository.save(payment), PaymentResponseDTO.class);
	}

	@Override
	public List<PaymentResponseDTO> getPaymentsByBooking(Long bookingId) {
		return paymentRepository.findByBookingIdAndIsDeletedFalse(bookingId)
				.stream()
				.map(payment -> modelMapper.map(payment, PaymentResponseDTO.class))
				.toList();
	}

	@Override
	public void softDeletePayment(Long paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.filter(p -> !p.isDeleted())
				.orElseThrow(() -> new PaymentException("Payment not found"));

		payment.setDeleted(true);
	}
}
