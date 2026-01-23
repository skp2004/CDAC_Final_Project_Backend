package com.rideongo.bms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.rideongo.bms_service.dtos.BikeRequestDTO;
import com.rideongo.bms_service.dtos.BikeResponseDTO;
import com.rideongo.bms_service.service.BikeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bikes")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class BikeController {

	private final BikeService bikeService;

	@PostMapping
	public ResponseEntity<BikeResponseDTO> createBike(
			@RequestBody @Valid BikeRequestDTO dto) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(bikeService.createBike(dto));
	}

	@GetMapping("/{bikeId}")
	public ResponseEntity<BikeResponseDTO> getBike(@PathVariable Long bikeId) {
		return ResponseEntity.ok(bikeService.getBikeById(bikeId));
	}

	@GetMapping
	public ResponseEntity<List<BikeResponseDTO>> getAllBikes() {
		return ResponseEntity.ok(bikeService.getAllBikes());
	}

	@PutMapping("/{bikeId}")
	public ResponseEntity<BikeResponseDTO> updateBike(
			@PathVariable Long bikeId,
			@RequestBody @Valid BikeRequestDTO dto) {

		return ResponseEntity.ok(bikeService.updateBike(bikeId, dto));
	}

	@DeleteMapping("/{bikeId}")
	public ResponseEntity<String> deleteBike(@PathVariable Long bikeId) {

		bikeService.deleteBike(bikeId);
		return ResponseEntity.ok("Bike deleted successfully");
	}
}
