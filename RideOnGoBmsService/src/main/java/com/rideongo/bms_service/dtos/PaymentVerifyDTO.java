package com.rideongo.bms_service.dtos;

import lombok.Data;

@Data
public class PaymentVerifyDTO {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private Long bookingId;
    private Double amount;
    private String paymentMode; // e.g., "CARD", "UPI"
}
