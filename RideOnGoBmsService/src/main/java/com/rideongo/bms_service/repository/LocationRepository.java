package com.rideongo.bms_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideongo.bms_service.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

	Optional<Location> findByIdAndIsDeletedFalse(Long id);

	List<Location> findByIsDeletedFalse();

	List<Location> findByIsDeletedFalseAndIsActiveTrue(); // useful for bikes

	boolean existsByAddressAndCityAndIsDeletedFalse(String address, String city);
}
