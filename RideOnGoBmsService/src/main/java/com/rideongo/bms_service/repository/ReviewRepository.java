package com.rideongo.bms_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideongo.bms_service.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByBikeIdAndIsDeletedFalse(Long bikeId);

	List<Review> findByUserIdAndIsDeletedFalse(Long userId);
}
