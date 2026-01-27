package com.rideongo.bms_service.service;

import java.util.List;

import com.rideongo.bms_service.dtos.LocationRequestDTO;
import com.rideongo.bms_service.dtos.LocationResponseDTO;

public interface LocationService {

	LocationResponseDTO addLocation(LocationRequestDTO dto);

	List<LocationResponseDTO> getAllLocations();

	LocationResponseDTO getLocationById(Long locationId);

	void softDeleteLocation(Long locationId);
}
