package com.rideongo.bms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideongo.bms_service.dtos.BikeResponseDTO;
import com.rideongo.bms_service.dtos.CityResponseDTO;
import com.rideongo.bms_service.dtos.LocationRequestDTO;
import com.rideongo.bms_service.dtos.LocationResponseDTO;
import com.rideongo.bms_service.service.BikeService;
import com.rideongo.bms_service.service.LocationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Validated
public class LocationController {

	private final LocationService locationService;
	private final BikeService bikeService;

	@PostMapping
	public ResponseEntity<LocationResponseDTO> addLocation(
			@RequestBody @Valid LocationRequestDTO dto) {
		return new ResponseEntity<>(locationService.addLocation(dto), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
		return ResponseEntity.ok(locationService.getAllLocations());
	}

	@GetMapping("/{id}")
	public ResponseEntity<LocationResponseDTO> getLocation(@PathVariable Long id) {
		return ResponseEntity.ok(locationService.getLocationById(id));
	}
	
	
	@GetMapping("/{locationId}/bikes")
	public ResponseEntity<List<BikeResponseDTO>> getBikesForLocation(
	        @PathVariable Long locationId) {

	    return ResponseEntity.ok(
	            bikeService.getBikesByLocationId(locationId)
	    );
	}


	@GetMapping("/cities") // 🟢 NEW
	public ResponseEntity<List<CityResponseDTO>> getAllCities() {

		return ResponseEntity.ok(
				locationService.getAllCities()
		);
	}


	@PutMapping("/{id}") 
	public ResponseEntity<LocationResponseDTO> updateLocation(
			@PathVariable Long id,
			@RequestBody @Valid LocationRequestDTO dto) {

		return ResponseEntity.ok(
				locationService.updateLocation(id, dto)
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
		locationService.softDeleteLocation(id);
		return ResponseEntity.ok("Location deleted successfully");
	}
	
	
}
