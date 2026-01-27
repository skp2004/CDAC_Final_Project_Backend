package com.rideongo.bms_service.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rideongo.bms_service.custom_exceptions.InvalidInputException;
import com.rideongo.bms_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.bms_service.dtos.LocationRequestDTO;
import com.rideongo.bms_service.dtos.LocationResponseDTO;
import com.rideongo.bms_service.entities.Location;
import com.rideongo.bms_service.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

	private final LocationRepository locationRepository;
	private final ModelMapper modelMapper;

	@Override
	public LocationResponseDTO addLocation(LocationRequestDTO dto) {

		if (locationRepository.existsByAddressAndCityAndIsDeletedFalse(
				dto.getAddress(), dto.getCity())) {
			throw new InvalidInputException("Location already exists");
		}

		Location location = modelMapper.map(dto, Location.class);
		return modelMapper.map(locationRepository.save(location), LocationResponseDTO.class);
	}

	@Override
	public List<LocationResponseDTO> getAllLocations() {
		return locationRepository.findByIsDeletedFalse()
				.stream()
				.map(loc -> modelMapper.map(loc, LocationResponseDTO.class))
				.toList();
	}

	@Override
	public LocationResponseDTO getLocationById(Long locationId) {
		Location location = locationRepository.findById(locationId)
				.filter(l -> !l.isDeleted())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid location id"));

		return modelMapper.map(location, LocationResponseDTO.class);
	}

	@Override
	public void softDeleteLocation(Long locationId) {
		Location location = locationRepository.findById(locationId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid location id"));

		location.setDeleted(true);
	}
}
