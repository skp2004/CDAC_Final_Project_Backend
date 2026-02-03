package com.rideongo.bms_service.service;

import java.util.List;

import com.rideongo.bms_service.dtos.ReviewRequestDTO;
import com.rideongo.bms_service.dtos.ReviewResponseDTO;

public interface ReviewService {

    ReviewResponseDTO addReview(
            ReviewRequestDTO dto,
            String authHeader // ORANGE
    );

    ReviewResponseDTO updateReview(
            Long reviewId,
            ReviewRequestDTO dto,
            String authHeader // ORANGE
    );

    List<ReviewResponseDTO> getReviewsByBike(Long bikeId);

    void softDeleteReview(
            Long reviewId,
            String authHeader // ORANGE
    );

    List<ReviewResponseDTO> getAllReviews();
}
