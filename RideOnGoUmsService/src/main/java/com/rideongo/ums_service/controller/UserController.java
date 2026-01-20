package com.rideongo.ums_service.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideongo.ums_service.dtos.AuthRequest;
import com.rideongo.ums_service.dtos.UserDTO;
import com.rideongo.ums_service.entities.User;
import com.rideongo.ums_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")  
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor 
@Validated
@Slf4j
public class UserController {

	private final UserService userService;	
	

	@GetMapping
	public ResponseEntity<?> renderUserList() {
		System.out.println("in render user list");
		List<UserDTO> list = userService.getAllUsers();
		if(list.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build(); //only status code : 204
		//=> non empty body
		return ResponseEntity.ok(list); //SC 200 + List -> Json[]
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserDetails(@PathVariable  @Min(1) @Max(100) Long userId) {
		System.out.println("in get user details "+userId);
	
			return ResponseEntity.ok(
					userService.getUserDetails(userId));
		
	}
	
	@PutMapping("/{id}")
	@Operation(description ="Complete Update user details")
	public ResponseEntity<?> updateUserDetails(@PathVariable Long id,@RequestBody User user) {
		System.out.println("in update "+id+" "+user);
	
			return ResponseEntity.ok(userService.updateDetails(id,user)
					);
		
	}
	
	@PostMapping("/signin")
	@Operation(description = "User Login")
	public ResponseEntity<?> userSignIn(@RequestBody @Valid  AuthRequest request) {
		log.info("***** in user sign in {} ",request);		
			return ResponseEntity.ok(userService.authenticate(request));		
	}
	
	@PatchMapping("/pwd-encryption")
	@Operation(description ="Encrypt Password of all users" )
	public ResponseEntity<?> encryptUserPassword() {
		log.info("encrypting users password ");
		//invoke service layer method
		return ResponseEntity.ok(userService.encryptPasswords());

	}

}
