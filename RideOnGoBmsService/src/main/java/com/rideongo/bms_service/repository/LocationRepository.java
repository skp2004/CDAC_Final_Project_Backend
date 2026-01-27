package com.rideongo.bms_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideongo.bms_service.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

	List<Location> findByIsDeletedFalse();

	boolean existsByAddressAndCityAndIsDeletedFalse(String address, String city);
}
