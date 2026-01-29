package com.rideongo.ums_service.dtos;

import java.time.LocalDate;

import com.rideongo.ums_service.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
	private Long id;	
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private UserRole userRole;
	private String phone;
	private String image;
	private boolean isVerified;
	
}
