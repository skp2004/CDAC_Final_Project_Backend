package com.rideongo.bms_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RazorpayOrderResponse {

    private String orderId;
    private Long bookingId;
    private Integer amount; // Amount in paise
    private String currency;
    private String razorpayKeyId;
    private String status;
}
