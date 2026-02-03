package com.rideongo.bms_service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RazorpayOrderRequest {

    @NotNull(message = "Booking id is required")
    private Long bookingId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private String currency = "INR";
}
