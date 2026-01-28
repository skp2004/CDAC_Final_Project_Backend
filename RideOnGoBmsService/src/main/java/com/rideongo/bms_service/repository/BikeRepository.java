package com.rideongo.bms_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideongo.bms_service.entities.Bike;
import com.rideongo.bms_service.entities.BikeStatus;

public interface BikeRepository extends JpaRepository<Bike, Long> {

	Optional<Bike> findByIdAndIsDeletedFalse(Long id);

	List<Bike> findAllByIsDeletedFalse();

	List<Bike> findByStatusAndIsDeletedFalse(BikeStatus status);
	List<Bike> findByLocation_IdAndIsDeletedFalse(Long locationId);
	// to count the total no of bike of respective brand
    long countByBrand_Id(Long brandId);
	// to count the total no of bike in respective location
    long countByLocation_Id(Long locationId);
	
}
