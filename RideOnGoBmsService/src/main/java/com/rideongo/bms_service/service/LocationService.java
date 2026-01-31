package com.rideongo.bms_service.service;

import java.util.List;

import com.rideongo.bms_service.dtos.BikeResponseDTO;
import com.rideongo.bms_service.dtos.CityResponseDTO;
import com.rideongo.bms_service.dtos.LocationRequestDTO;
import com.rideongo.bms_service.dtos.LocationResponseDTO;

public interface LocationService {

	LocationResponseDTO addLocation(LocationRequestDTO dto);

	List<LocationResponseDTO> getAllLocations();

	LocationResponseDTO getLocationById(Long locationId);
	List<CityResponseDTO> getAllCities();         
	List<BikeResponseDTO> getBikesByCity(String city);

	LocationResponseDTO updateLocation(Long locationId, LocationRequestDTO dto);

	void softDeleteLocation(Long locationId);
}
