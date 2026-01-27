package com.rideongo.bms_service.service;

import java.util.List;

import com.rideongo.bms_service.dtos.ReviewRequestDTO;
import com.rideongo.bms_service.dtos.ReviewResponseDTO;

public interface ReviewService {

	ReviewResponseDTO addReview(ReviewRequestDTO dto, Long userId);

	List<ReviewResponseDTO> getReviewsByBike(Long bikeId);

	void softDeleteReview(Long reviewId, Long userId);
}
