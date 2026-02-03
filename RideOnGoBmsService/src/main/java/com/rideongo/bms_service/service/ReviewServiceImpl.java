package com.rideongo.bms_service.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rideongo.bms_service.clients.UmsClient;
import com.rideongo.bms_service.custom_exceptions.ReviewNotFoundException;
import com.rideongo.bms_service.dtos.ReviewRequestDTO;
import com.rideongo.bms_service.dtos.ReviewResponseDTO;
import com.rideongo.bms_service.entities.Review;
import com.rideongo.bms_service.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

        private final ReviewRepository reviewRepository;
        private final ModelMapper modelMapper;
        private final UmsClient umsClient;

        @Override
        public ReviewResponseDTO addReview(
                        ReviewRequestDTO dto,
                        String authHeader) {

                Long userId = umsClient.getLoggedInUserId(authHeader);

                Review review = new Review();
                review.setRating(dto.getRating());
                review.setComments(dto.getComments());
                review.setBikeId(dto.getBikeId());
                review.setUserId(userId);

                return modelMapper.map(
                                reviewRepository.save(review),
                                ReviewResponseDTO.class);
        }

        @Override
        public ReviewResponseDTO updateReview(
                        Long reviewId,
                        ReviewRequestDTO dto,
                        String authHeader) {

                Long userId = umsClient.getLoggedInUserId(authHeader);

                Review review = reviewRepository.findById(reviewId)
                                .filter(r -> !r.isDeleted() && r.getUserId().equals(userId))
                                .orElseThrow(() -> new ReviewNotFoundException("Unauthorized"));

                review.setRating(dto.getRating());
                review.setComments(dto.getComments());

                return modelMapper.map(review, ReviewResponseDTO.class);
        }

        @Override
        public void softDeleteReview(
                        Long reviewId,
                        String authHeader) {

                Long userId = umsClient.getLoggedInUserId(authHeader);

                Review review = reviewRepository.findById(reviewId)
                                .filter(r -> !r.isDeleted() && r.getUserId().equals(userId))
                                .orElseThrow(() -> new ReviewNotFoundException("Review not found or unauthorized"));

                review.setDeleted(true);
        }

        @Override
        public List<ReviewResponseDTO> getReviewsByBike(Long bikeId) {
                return reviewRepository.findByBikeIdAndIsDeletedFalse(bikeId)
                                .stream()
                                .map(r -> modelMapper.map(r, ReviewResponseDTO.class))
                                .toList();
        }

        @Override
        public List<ReviewResponseDTO> getAllReviews() {
                return reviewRepository.findByIsDeletedFalse()
                                .stream()
                                .map(r -> modelMapper.map(r, ReviewResponseDTO.class))
                                .toList();
        }
}
