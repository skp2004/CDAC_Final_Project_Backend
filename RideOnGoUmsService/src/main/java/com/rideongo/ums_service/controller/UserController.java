package com.rideongo.ums_service.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rideongo.ums_service.dtos.AdminSignupRequest;
import com.rideongo.ums_service.dtos.AuthRequest;
import com.rideongo.ums_service.dtos.UserDTO;
import com.rideongo.ums_service.dtos.UserProfileResponseDTO;
import com.rideongo.ums_service.dtos.UserSignupRequest;
import com.rideongo.ums_service.entities.User;
import com.rideongo.ums_service.security.JwtUtils;
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
	private final JwtUtils jwtUtils;


	@GetMapping
	public ResponseEntity<?> renderUserList() {
		System.out.println("in render user list");
		List<UserDTO> list = userService.getAllUsers();
		if (list.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserDetails(@PathVariable @Min(1) @Max(100) Long userId) {
		System.out.println("in get user details " + userId);

		return ResponseEntity.ok(userService.getUserDetails(userId));

	}

	@PutMapping("/{id}")
	@Operation(description = "Complete Update user details")
	public ResponseEntity<?> updateUserDetails(@PathVariable Long id, @RequestBody User user) {
		System.out.println("in update " + id + " " + user);

		return ResponseEntity.ok(userService.updateDetails(id, user));

	}

	@PostMapping("/signin")
	@Operation(description = "User Login")
	public ResponseEntity<?> userSignIn(@RequestBody @Valid AuthRequest request) {
		log.info("***** in user sign in {} ", request);
		return ResponseEntity.ok(userService.authenticate(request));
	}

	@PatchMapping("/pwd-encryption")
	@Operation(description = "Encrypt Password of all users")
	public ResponseEntity<?> encryptUserPassword() {
		log.info("encrypting users password ");

		return ResponseEntity.ok(userService.encryptPasswords());

	}

	@PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(description = "Customer Signup with Image, Aadhaar and License uploads")
	public ResponseEntity<?> userSignup(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("phone") String phone,
			@RequestParam(value = "dob", required = false) String dob,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
			@RequestPart(value = "aadhaarImage", required = false) MultipartFile aadhaarImage,
			@RequestPart(value = "licenseImage", required = false) MultipartFile licenseImage) throws IOException {

		UserSignupRequest request = new UserSignupRequest();
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setEmail(email);
		request.setPassword(password);
		request.setPhone(phone);
		if (dob != null && !dob.isEmpty()) {
			request.setDob(java.time.LocalDate.parse(dob));
		}

		log.info("Customer signup request for email: {}", request.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(userService.userSignup(request, profileImage, aadhaarImage, licenseImage));
	}

	@PostMapping(value = "/admin/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(description = "Admin Signup with Profile Image")
	public ResponseEntity<?> adminSignup(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("phone") String phone,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

		AdminSignupRequest request = new AdminSignupRequest();
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setEmail(email);
		request.setPassword(password);
		request.setPhone(phone);

		log.info("Admin signup request for email: {}", request.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.adminSignup(request, profileImage));
	}
	
	@GetMapping("/me")
	@Operation(description = "Get logged-in user profile from JWT token")
	public ResponseEntity<UserProfileResponseDTO> getLoggedInUserProfile(
			@RequestHeader("Authorization") String authHeader) {

		// Extract token
		String token = authHeader.substring(7); // remove "Bearer "

		// Validate & parse token
		String email = jwtUtils.validateToken(token).getSubject();

		log.info("Logged in user email from JWT: {}", email);

		return ResponseEntity.ok(
				userService.getLoggedInUserProfile(email)
		);
	}
}




