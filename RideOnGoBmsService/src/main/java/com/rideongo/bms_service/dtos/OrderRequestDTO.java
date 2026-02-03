package com.rideongo.bms_service.dtos;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long bookingId;
    private Double amount;
}
