package com.rideongo.bms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rideongo.bms_service.dtos.BrandRequestDTO;
import com.rideongo.bms_service.dtos.BrandResponseDTO;
import com.rideongo.bms_service.service.BrandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class BrandController {

	private final BrandService brandService;

	@PostMapping
	public ResponseEntity<BrandResponseDTO> createBrand(
			@RequestBody @Valid BrandRequestDTO dto) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(brandService.createBrand(dto));
	}

	@GetMapping("/{brandId}")
	public ResponseEntity<BrandResponseDTO> getBrand(@PathVariable Long brandId) {

		return ResponseEntity.ok(brandService.getBrandById(brandId));
	}

	@GetMapping
	public ResponseEntity<List<BrandResponseDTO>> getAllBrands() {

		return ResponseEntity.ok(brandService.getAllBrands());
	}

	@PutMapping("/{brandId}")
	public ResponseEntity<BrandResponseDTO> updateBrand(
			@PathVariable Long brandId,
			@RequestBody @Valid BrandRequestDTO dto) {

		return ResponseEntity.ok(brandService.updateBrand(brandId, dto));
	}

	@DeleteMapping("/{brandId}")
	public ResponseEntity<?> deleteBrand(@PathVariable Long brandId) {

		brandService.deleteBrand(brandId);
		return ResponseEntity.ok("Brand deleted successfully");
	}
	@GetMapping("/active")
	public ResponseEntity<List<BrandResponseDTO>> getActiveBrands() {
		return ResponseEntity.ok(brandService.getActiveBrands());
	}

	@PatchMapping("/{brandId}/status")
	public ResponseEntity<String> toggleBrandStatus(
			@PathVariable Long brandId,
			@RequestParam Boolean isActive) {

		brandService.toggleBrandStatus(brandId, isActive);
		return ResponseEntity.ok("Brand status updated");
	}
}
