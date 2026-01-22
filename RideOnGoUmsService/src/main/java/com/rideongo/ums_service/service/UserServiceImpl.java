package com.rideongo.ums_service.service;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rideongo.ums_service.custom_exceptions.InvalidInputException;
import com.rideongo.ums_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.ums_service.dtos.AdminSignupRequest;
import com.rideongo.ums_service.dtos.ApiResponse;
import com.rideongo.ums_service.dtos.AuthRequest;
import com.rideongo.ums_service.dtos.AuthResp;
import com.rideongo.ums_service.dtos.UserDTO;
import com.rideongo.ums_service.dtos.UserSignupRequest;
import com.rideongo.ums_service.entities.User;
import com.rideongo.ums_service.entities.UserRole;
import com.rideongo.ums_service.repository.UserRepository;
import com.rideongo.ums_service.security.JwtUtils;
import com.rideongo.ums_service.security.UserPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@Transactional 
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final CloudinaryService cloudinaryService;
	@Override
	public List<UserDTO> getAllUsers() {

		return userRepository.findAll() 
				.stream() 
				.map(entity -> modelMapper.map(entity, UserDTO.class))
				.toList();
	}

	@Override
	public String addUser(User user) {

		if (userRepository.existsByEmailOrPhone(user.getEmail(), user.getPhone())) {

			throw new InvalidInputException("Dup email or phone !!!!!!!!");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// 3. Save user
		User persistentUser = userRepository.save(user);

		return "New User added with ID=" + persistentUser.getId();
	}

	@Override
	public ApiResponse deleteUserDetails(Long userId) {

		if (userRepository.existsById(userId)) {

			userRepository.deleteById(userId);
			return new ApiResponse("Success", "User details deleted ....");
		}

		throw new ResourceNotFoundException("User doesn't exist by id !!!!!");
	}

	@Override
	public User getUserDetails(Long userId) {

		return userRepository.findById(userId) 
				.orElseThrow(() -> new ResourceNotFoundException("Invalid user id !!!!!"));
	}

	@Override
	public ApiResponse updateDetails(Long id, User user) {

		User persistentUser = getUserDetails(id);

		persistentUser.setDob(user.getDob());
		persistentUser.setFirstName(user.getFirstName());
		persistentUser.setLastName(user.getLastName());
		persistentUser.setPassword(user.getPassword());

		return new ApiResponse("Success", "Updated user details");
	}

	@Override
	public AuthResp authenticate(AuthRequest request) {
		System.out.println("in user sign in " + request);

		Authentication holder = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
		log.info("*****Before -  is authenticated {}", holder.isAuthenticated());// false
	
		Authentication fullyAuth = authenticationManager.authenticate(holder);
		
		log.info("*****After -  is authenticated {}", fullyAuth.isAuthenticated());// true
		log.info("**** auth {} ", fullyAuth);// principal : user details , null : pwd , Collection<GrantedAuth>
		log.info("***** class of principal {}", fullyAuth.getPrincipal().getClass());// com.healthcare.security.UserPrincipal
	
		UserPrincipal principal = (UserPrincipal) fullyAuth.getPrincipal();
		return new AuthResp(jwtUtils.generateToken(principal), "Successful Login");

	}

	@Override
	public ApiResponse encryptPasswords() {
		
		List<User> users = userRepository.findAll();
		
		users.forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
		return new ApiResponse("Password encrypted", "Success");
	}

	@Override
	public ApiResponse userSignup(UserSignupRequest request, 
	                               MultipartFile profileImage, 
	                               MultipartFile aadhaarImage, 
	                               MultipartFile licenseImage) throws IOException {
		
		log.info("Processing customer signup for email: {}", request.getEmail());
		
		if (userRepository.existsByEmailOrPhone(request.getEmail(), request.getPhone())) {
			throw new InvalidInputException("Email or phone already exists");
		}

		User user = modelMapper.map(request, User.class);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setUserRole(UserRole.ROLE_CUSTOMER);
		user.setVerified(false);
		user.setDeleted(false);
		user.setKycStatus("PENDING");

		// Upload images to Cloudinary
		try {
			if (profileImage != null && !profileImage.isEmpty()) {
				String profileImageUrl = cloudinaryService.uploadImage(
						profileImage, 
						"rideongo/users/profiles"
				);
				user.setImage(profileImageUrl);
				log.info("Profile image uploaded successfully");
			}

			if (aadhaarImage != null && !aadhaarImage.isEmpty()) {
				String aadhaarUrl = cloudinaryService.uploadImage(
						aadhaarImage, 
						"rideongo/users/aadhaar"
				);
				user.setAadhaarUrl(aadhaarUrl);
				log.info("Aadhaar image uploaded successfully");
			}

			if (licenseImage != null && !licenseImage.isEmpty()) {
				String licenseUrl = cloudinaryService.uploadImage(
						licenseImage, 
						"rideongo/users/licenses"
				);
				user.setLicenceUrl(licenseUrl);
				log.info("License image uploaded successfully");
			}

			userRepository.save(user);
			log.info("Customer registered successfully with ID: {}", user.getId());
			
			return new ApiResponse("SUCCESS", "Customer registered successfully");
			
		} catch (IOException e) {
			log.error("Error uploading images during customer signup", e);
			throw new IOException("Failed to upload images: " + e.getMessage());
		}
	}

	@Override
	public ApiResponse adminSignup(AdminSignupRequest request, 
	                                MultipartFile profileImage) throws IOException {
		
		log.info("Processing admin signup for email: {}", request.getEmail());
		
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new InvalidInputException("Admin email already exists");
		}

		User admin = new User();
		admin.setFirstName(request.getFirstName());
		admin.setLastName(request.getLastName());
		admin.setEmail(request.getEmail());
		admin.setPhone(request.getPhone());
		admin.setPassword(passwordEncoder.encode(request.getPassword()));
		admin.setUserRole(UserRole.ROLE_ADMIN);
		admin.setVerified(true);
		admin.setDeleted(false);

		// Upload profile image to Cloudinary
		try {
			if (profileImage != null && !profileImage.isEmpty()) {
				String profileImageUrl = cloudinaryService.uploadImage(
						profileImage, 
						"rideongo/admins/profiles"
				);
				admin.setImage(profileImageUrl);
				log.info("Admin profile image uploaded successfully");
			}

			userRepository.save(admin);
			log.info("Admin registered successfully with ID: {}", admin.getId());
			
			return new ApiResponse("SUCCESS", "Admin registered successfully");
			
		} catch (IOException e) {
			log.error("Error uploading profile image during admin signup", e);
			throw new IOException("Failed to upload profile image: " + e.getMessage());
		}
	}

}
