package com.rideongo.bms_service.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideongo.bms_service.dtos.BikeRequestDTO;
import com.rideongo.bms_service.dtos.BikeResponseDTO;
import com.rideongo.bms_service.dtos.BikeStatusUpdateDTO;
import com.rideongo.bms_service.service.BikeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bikes")
@RequiredArgsConstructor
@Validated
public class BikeController {

	private final BikeService bikeService;
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity createBike(
		    @RequestPart("data") BikeRequestDTO dto,
		    @RequestPart(value = "image", required = false) MultipartFile image
		) throws IOException {
		    return ResponseEntity.status(HttpStatus.CREATED)
		        .body(bikeService.createBike(dto, image));
		}


	@PatchMapping("/{bikeId}/status") // NEW
	public ResponseEntity<String> updateBikeStatus(
			@PathVariable Long bikeId,
			@RequestBody @Valid BikeStatusUpdateDTO dto) {

		bikeService.updateBikeStatus(bikeId, dto.getStatus());
		return ResponseEntity.ok("Bike status updated");
	}

	@GetMapping("/location/{locationId}") // NEW
	public ResponseEntity<List<BikeResponseDTO>> getBikesByLocation(
			@PathVariable Long locationId) {

		return ResponseEntity.ok(
				bikeService.getBikesByLocation(locationId)
		);
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
