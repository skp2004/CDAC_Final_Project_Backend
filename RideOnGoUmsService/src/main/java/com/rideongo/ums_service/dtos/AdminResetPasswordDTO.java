package com.rideongo.ums_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResetPasswordDTO {

    @NotBlank(message = "New password is required")
    private String newPassword;
}
