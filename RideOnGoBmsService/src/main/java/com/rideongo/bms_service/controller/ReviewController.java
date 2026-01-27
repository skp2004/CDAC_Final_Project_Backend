package com.rideongo.bms_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideongo.bms_service.dtos.ReviewResponseDTO;
import com.rideongo.bms_service.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

	private final ReviewService reviewService;

//	@PostMapping
//	public ResponseEntity<ReviewResponseDTO> addReview(
//			@RequestBody @Valid ReviewRequestDTO dto,
//			Authentication authentication) {
//
//		Long userId = Long.parseLong(authentication.getName());
//		return new ResponseEntity<>(
//				reviewService.addReview(dto, userId),
//				HttpStatus.CREATED
//		);
//	}

	@GetMapping("/bike/{bikeId}")
	public ResponseEntity<List<ReviewResponseDTO>> getReviewsByBike(
			@PathVariable Long bikeId) {
		return ResponseEntity.ok(reviewService.getReviewsByBike(bikeId));
	}
//	@DeleteMapping("/{reviewId}")
//	public ResponseEntity<?> deleteReview(
//	        @PathVariable Long reviewId,
//	        @RequestHeader("Authorization") String authHeader) {
//
//	    String token = authHeader.substring(7); // Bearer xxx
//	    Long userId = jwtUtils.extractUserId(token);
//
//	    reviewService.softDeleteReview(reviewId, userId);
//	    return ResponseEntity.ok("Review deleted successfully");
//	}


}
