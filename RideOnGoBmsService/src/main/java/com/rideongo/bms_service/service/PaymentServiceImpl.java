package com.rideongo.bms_service.service;

import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.rideongo.bms_service.config.RazorpayConfig;
import com.rideongo.bms_service.custom_exceptions.BookingNotFoundException;
import com.rideongo.bms_service.custom_exceptions.PaymentException;
import com.rideongo.bms_service.dtos.PaymentRequestDTO;
import com.rideongo.bms_service.dtos.PaymentResponseDTO;
import com.rideongo.bms_service.dtos.RazorpayOrderRequest;
import com.rideongo.bms_service.dtos.RazorpayOrderResponse;
import com.rideongo.bms_service.dtos.RazorpayPaymentVerification;
import com.rideongo.bms_service.entities.Booking;
import com.rideongo.bms_service.entities.Payment;
import com.rideongo.bms_service.repository.BookingRepository;
import com.rideongo.bms_service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final BookingRepository bookingRepository;
	private final ModelMapper modelMapper;
	private final RazorpayClient razorpayClient;
	private final RazorpayConfig razorpayConfig;

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
	public List<PaymentResponseDTO> getAllPayments() {
		return paymentRepository.findAllByIsDeletedFalse()
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

	@Override
	public RazorpayOrderResponse createRazorpayOrder(RazorpayOrderRequest request) {

		// Verify booking exists
		Booking booking = bookingRepository.findByIdAndIsDeletedFalse(request.getBookingId())
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		try {
			// Convert amount to paise (Razorpay requires amount in smallest currency unit)
			int amountInPaise = (int) (request.getAmount() * 100);

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amountInPaise);
			orderRequest.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
			orderRequest.put("receipt", "booking_" + request.getBookingId());
			orderRequest.put("notes", new JSONObject().put("bookingId", request.getBookingId()));

			Order order = razorpayClient.orders.create(orderRequest);

			String orderId = order.get("id");

			// Update booking with Razorpay order ID
			booking.setRazorpayOrderId(orderId);
			bookingRepository.save(booking);

			return RazorpayOrderResponse.builder()
					.orderId(orderId)
					.bookingId(request.getBookingId())
					.amount(amountInPaise)
					.currency(request.getCurrency() != null ? request.getCurrency() : "INR")
					.razorpayKeyId(razorpayConfig.getKeyId())
					.status(order.get("status"))
					.build();

		} catch (RazorpayException e) {
			log.error("Failed to create Razorpay order: {}", e.getMessage());
			throw new PaymentException("Failed to create payment order: " + e.getMessage());
		}
	}

	@Override
	public PaymentResponseDTO verifyAndSavePayment(RazorpayPaymentVerification request) {

		// Verify booking exists
		Booking booking = bookingRepository.findByIdAndIsDeletedFalse(request.getBookingId())
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		// Verify signature
		boolean isValidSignature = verifyRazorpaySignature(
				request.getRazorpayOrderId(),
				request.getRazorpayPaymentId(),
				request.getRazorpaySignature());

		if (!isValidSignature) {
			throw new PaymentException("Invalid payment signature. Payment verification failed.");
		}

		// Check for duplicate payment
		if (paymentRepository.existsByTxnRef(request.getRazorpayPaymentId())) {
			throw new PaymentException("Payment already processed");
		}

		// Save payment record
		Payment payment = new Payment();
		payment.setBookingId(request.getBookingId());
		payment.setAmount(booking.getTotalAmount());
		payment.setPaymentMode(request.getPaymentMode() != null ? request.getPaymentMode() : "RAZORPAY");
		payment.setPaymentType("FULL");
		payment.setTxnRef(request.getRazorpayPaymentId());
		payment.setStatus("SUCCESS");

		Payment savedPayment = paymentRepository.save(payment);

		return modelMapper.map(savedPayment, PaymentResponseDTO.class);
	}

	private boolean verifyRazorpaySignature(String orderId, String paymentId, String signature) {
		try {
			String data = orderId + "|" + paymentId;
			String generatedSignature = hmacSha256(data, razorpayConfig.getKeySecret());
			return generatedSignature.equals(signature);
		} catch (Exception e) {
			log.error("Signature verification failed: {}", e.getMessage());
			return false;
		}
	}

	private String hmacSha256(String data, String secret) throws Exception {
		Mac sha256Hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
		sha256Hmac.init(secretKey);
		byte[] hash = sha256Hmac.doFinal(data.getBytes("UTF-8"));
		StringBuilder hexString = new StringBuilder();
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
