package com.rideongo.bms_service.service;

import java.util.List;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.rideongo.bms_service.custom_exceptions.PaymentException;
import com.rideongo.bms_service.dtos.PaymentRequestDTO;
import com.rideongo.bms_service.dtos.PaymentResponseDTO;
import com.rideongo.bms_service.entities.Payment;
import com.rideongo.bms_service.repository.PaymentRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final ModelMapper modelMapper;

	@Value("${razorpay.key.id}")
	private String razorpayKeyId;

	@Value("${razorpay.key.secret}")
	private String razorpayKeySecret;

	private RazorpayClient razorpayClient;

	@PostConstruct
	public void init() throws RazorpayException {
		this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
	}

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
	public com.rideongo.bms_service.dtos.OrderResponseDTO createOrder(
			com.rideongo.bms_service.dtos.OrderRequestDTO dto) {

		try {
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", dto.getAmount() * 100); // Amount in paise
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

			Order order = razorpayClient.orders.create(orderRequest);

			return new com.rideongo.bms_service.dtos.OrderResponseDTO(
					order.get("id").toString(),
					dto.getAmount(),
					"INR",
					razorpayKeyId // Return Real Key ID
			);

		} catch (RazorpayException e) {
			log.error("Razorpay Order Creation Failed", e);
			throw new PaymentException("Failed to create Razorpay order: " + e.getMessage());
		}
	}

	@Override
	public PaymentResponseDTO verifyPayment(com.rideongo.bms_service.dtos.PaymentVerifyDTO dto) {

		try {
			String signaturePayload = dto.getRazorpayOrderId() + "|" + dto.getRazorpayPaymentId();

			boolean isValid = Utils.verifyPaymentSignature(
					new JSONObject()
							.put("razorpay_order_id", dto.getRazorpayOrderId())
							.put("razorpay_payment_id", dto.getRazorpayPaymentId())
							.put("razorpay_signature", dto.getRazorpaySignature()),
					razorpayKeySecret);

			if (!isValid) {
				throw new PaymentException("Invalid Payment Signature");
			}

			// Save success payment
			Payment payment = new Payment();
			payment.setBookingId(dto.getBookingId());
			payment.setAmount(dto.getAmount());
			payment.setPaymentMode(dto.getPaymentMode() != null ? dto.getPaymentMode() : "ONLINE");
			payment.setPaymentType("FULL");
			payment.setTxnRef(dto.getRazorpayPaymentId());
			payment.setStatus("SUCCESS");

			return modelMapper.map(paymentRepository.save(payment), PaymentResponseDTO.class);

		} catch (RazorpayException e) {
			throw new PaymentException("Payment Verification Failed: " + e.getMessage());
		}
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
		return paymentRepository.findByIsDeletedFalse()
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
