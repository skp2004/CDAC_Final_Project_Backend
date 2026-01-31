package com.rideongo.bms_service.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rideongo.bms_service.custom_exceptions.InvalidInputException;
import com.rideongo.bms_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.bms_service.dtos.BikeResponseDTO;
import com.rideongo.bms_service.dtos.CityResponseDTO;
import com.rideongo.bms_service.dtos.LocationRequestDTO;
import com.rideongo.bms_service.dtos.LocationResponseDTO;
import com.rideongo.bms_service.entities.Location;
import com.rideongo.bms_service.repository.BikeRepository;
import com.rideongo.bms_service.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

	private final LocationRepository locationRepository;
	private final ModelMapper modelMapper;
	private final BikeRepository bikeRepository;

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
    public LocationResponseDTO getLocationById(Long locationId) {

        Location location = locationRepository.findById(locationId)
                .filter(l -> !l.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid location id"));

        LocationResponseDTO dto =
                modelMapper.map(location, LocationResponseDTO.class);

        long total = bikeRepository.countByLocation_IdAndIsDeletedFalse(locationId);
        dto.setTotalBikes(total);   // ✅ inject extra field

        return dto;
    }

    @Override
    public List<LocationResponseDTO> getAllLocations() {

        return locationRepository.findByIsDeletedFalse()
                .stream()
                .map(loc -> {
                    LocationResponseDTO dto =
                            modelMapper.map(loc, LocationResponseDTO.class);

                    long total =
                            bikeRepository.countByLocation_IdAndIsDeletedFalse(loc.getId());

                    dto.setTotalBikes(total);
                    return dto;
                })
                .toList();
    }
    @Override
    public List<CityResponseDTO> getAllCities() { 

    	return locationRepository.findByIsDeletedFalseAndIsActiveTrue()
    			.stream()
    			.map(loc -> new CityResponseDTO(
    					loc.getId(),
    					loc.getCity()
    			))
    			.toList();
    }

    @Override
    public List<BikeResponseDTO> getBikesByCity(String city) { 

    	return bikeRepository.findByLocation_CityAndIsDeletedFalse(city)
    			.stream()
    			.map(bike -> new BikeResponseDTO(
    					bike.getId(),
    					bike.getBrand().getBrandName(),
    					bike.getLocation().getCity(),
    					bike.getLocation().getState(),
    					bike.getCc(),
    					bike.getColour(),
    					bike.getMileage(),
    					bike.getRatePerHour(),
    					bike.getRatePerDay(),
    					bike.getFuelType().name(),
    					bike.getStatus().name(),
    					bike.getImage()
    			))
    			.toList();
    }


    @Override
    public LocationResponseDTO updateLocation(Long locationId, LocationRequestDTO dto) { // 🟠 NEW

    	Location location = locationRepository.findByIdAndIsDeletedFalse(locationId)
    			.orElseThrow(() -> new ResourceNotFoundException("Invalid location id"));

    	// check duplicate (same city + address, excluding self) // 🟠 NEW
    	boolean exists =
    			locationRepository.existsByAddressAndCityAndIsDeletedFalse(
    					dto.getAddress(), dto.getCity());

    	if (exists &&
    		(!location.getAddress().equals(dto.getAddress())
    		|| !location.getCity().equals(dto.getCity()))) {
    		throw new InvalidInputException("Location already exists");
    	}

    	location.setAddress(dto.getAddress());
    	location.setCity(dto.getCity());
    	location.setState(dto.getState());
    	location.setPincode(dto.getPincode());
    	location.setContactNumber(dto.getContactNumber());
    	location.setActive(dto.getIsActive());

    	LocationResponseDTO response =
    			modelMapper.map(location, LocationResponseDTO.class);

    	long total = bikeRepository.countByLocation_IdAndIsDeletedFalse(locationId); // 🟠 NEW
    	response.setTotalBikes(total);

    	return response;
    }

	
	@Override
	public void softDeleteLocation(Long locationId) {

		Location location = locationRepository.findByIdAndIsDeletedFalse(locationId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid location id"));

		long bikeCount =
				bikeRepository.countByLocation_IdAndIsDeletedFalse(locationId);

		if (bikeCount > 0) { 
			throw new InvalidInputException(
					"Cannot delete location. Bikes are associated with this location");
		}

		location.setIsDeleted(true); // soft delete
	}
}
