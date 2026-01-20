package com.rideongo.ums_service.service;

import java.util.List;

import com.rideongo.ums_service.dtos.ApiResponse;
import com.rideongo.ums_service.dtos.AuthRequest;
import com.rideongo.ums_service.dtos.AuthResp;
import com.rideongo.ums_service.dtos.UserDTO;
import com.rideongo.ums_service.entities.User;


public interface UserService {
//get all users
	List<UserDTO> getAllUsers();

	String addUser(User user);

	ApiResponse deleteUserDetails(Long userId);

	User getUserDetails(Long userId);

	ApiResponse updateDetails(Long id, User user);

	AuthResp authenticate(AuthRequest request);
	ApiResponse encryptPasswords();
}
