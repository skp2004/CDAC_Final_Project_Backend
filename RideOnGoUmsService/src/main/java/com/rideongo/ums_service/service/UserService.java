package com.rideongo.ums_service.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rideongo.ums_service.dtos.AdminSignupRequest;
import com.rideongo.ums_service.dtos.ApiResponse;
import com.rideongo.ums_service.dtos.AuthRequest;
import com.rideongo.ums_service.dtos.AuthResp;
import com.rideongo.ums_service.dtos.UserDTO;
import com.rideongo.ums_service.dtos.UserProfileResponseDTO;
import com.rideongo.ums_service.dtos.UserSignupRequest;
import com.rideongo.ums_service.entities.User;

public interface UserService {
//get all users
	List<UserDTO> getAllUsers();

	String addUser(User user);

	ApiResponse deleteUserDetails(Long userId);

	User getUserDetails(Long userId);

	ApiResponse updateDetails(Long id, User user);

	AuthResp authenticate(AuthRequest request);
	AuthResp authenticateAdmin(AuthRequest request);
	ApiResponse encryptPasswords();

	ApiResponse userSignup(UserSignupRequest request, 
	                       MultipartFile profileImage, 
	                       MultipartFile aadhaarImage, 
	                       MultipartFile licenseImage) throws IOException;
	
	ApiResponse adminSignup(AdminSignupRequest request, 
	                        MultipartFile profileImage) throws IOException;
	
	UserProfileResponseDTO getLoggedInUserProfile(String email);
}
