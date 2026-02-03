package com.rideongo.bms_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideongo.bms_service.dtos.ReviewRequestDTO;
import com.rideongo.bms_service.dtos.ReviewResponseDTO;
import com.rideongo.bms_service.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> addReview(
            @RequestBody @Valid ReviewRequestDTO dto,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(
                reviewService.addReview(dto, authHeader));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewRequestDTO dto,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(
                reviewService.updateReview(reviewId, dto, authHeader));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String authHeader) {
        reviewService.softDeleteReview(reviewId, authHeader);
        return ResponseEntity.ok("Review deleted successfully");
    }

    @GetMapping("/bike/{bikeId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByBike(
            @PathVariable Long bikeId) {
        return ResponseEntity.ok(reviewService.getReviewsByBike(bikeId));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}
