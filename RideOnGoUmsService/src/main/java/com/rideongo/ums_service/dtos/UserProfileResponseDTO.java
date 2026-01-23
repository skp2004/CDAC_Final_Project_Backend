package com.rideongo.ums_service.dtos;


import java.time.LocalDate;

import com.rideongo.ums_service.entities.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponseDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private LocalDate dob;
	private UserRole userRole;
	private String image;
	private boolean isVerified;
	private String kycStatus;
	private String aadhaarUrl;
	private String licenceUrl;
}
