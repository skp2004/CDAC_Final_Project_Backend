package com.rideongo.bms_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideongo.bms_service.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	boolean existsByBrandNameAndIsDeletedFalse(String brandName);

	Optional<Brand> findByIdAndIsDeletedFalse(Long id);
	List<Brand> findAllByIsDeletedFalse();
	Optional<Brand> findByBrandNameAndIsDeletedFalse(String brandName);
	List<Brand> findAllByIsDeletedFalseAndIsActiveTrue();

}
