package com.rideongo.bms_service.service;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rideongo.bms_service.custom_exceptions.BikeNotFoundException;
import com.rideongo.bms_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.bms_service.dtos.BikeRequestDTO;
import com.rideongo.bms_service.dtos.BikeResponseDTO;
import com.rideongo.bms_service.entities.Bike;
import com.rideongo.bms_service.entities.BikeStatus;
import com.rideongo.bms_service.entities.Brand;
import com.rideongo.bms_service.entities.FuelType;
import com.rideongo.bms_service.entities.Location;
import com.rideongo.bms_service.repository.BikeRepository;
import com.rideongo.bms_service.repository.BrandRepository;
import com.rideongo.bms_service.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BikeServiceImpl implements BikeService {

	private final BikeRepository bikeRepository;
	private final BrandRepository brandRepository;
	private final ModelMapper modelMapper;
	private final LocationRepository locationRepository; 
	private final CloudinaryService cloudinaryService; 

	@Override
	public BikeResponseDTO createBike(BikeRequestDTO dto, MultipartFile image) throws IOException {

		Brand brand = brandRepository.findByIdAndIsDeletedFalse(dto.getBrandId())
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

		Location location = locationRepository.findByIdAndIsDeletedFalse(dto.getLocationId())
				.orElseThrow(() -> new ResourceNotFoundException("Location not found"));

		Bike bike = new Bike();
		bike.setBrand(brand);
		bike.setLocation(location);
		bike.setCc(dto.getCc());
		bike.setColour(dto.getColour());
		bike.setMileage(dto.getMileage());
		bike.setRatePerHour(dto.getRatePerHour());
		bike.setRatePerDay(dto.getRatePerDay());
		bike.setFuelType(FuelType.valueOf(dto.getFuelType()));
		bike.setStatus(BikeStatus.valueOf(dto.getStatus()));

		if (image != null && !image.isEmpty()) { // NEW
			String imageUrl = cloudinaryService.uploadImage(
					image,
					"rideongo/bikes"
			);
			bike.setImage(imageUrl);
		}

		return mapToResponse(bikeRepository.save(bike));
	}
	
	@Override
	public void updateBikeStatus(Long bikeId, String status) {

		Bike bike = bikeRepository.findByIdAndIsDeletedFalse(bikeId)
				.orElseThrow(() -> new ResourceNotFoundException("Bike not found"));

		bike.setStatus(BikeStatus.valueOf(status));
	}

	@Override
	public List<BikeResponseDTO> getBikesByLocation(Long locationId) {

		return bikeRepository.findByLocation_IdAndIsDeletedFalse(locationId)
				.stream()
				.map(this::mapToResponse)
				.toList();
	}


	@Override
	public BikeResponseDTO updateBike(Long bikeId, BikeRequestDTO dto) {

		Bike bike = bikeRepository.findByIdAndIsDeletedFalse(bikeId)
				.orElseThrow(() -> new BikeNotFoundException("Bike not found"));

		bike.setCc(dto.getCc());
		bike.setColour(dto.getColour());
		bike.setMileage(dto.getMileage());
		bike.setRatePerHour(dto.getRatePerHour());
		bike.setRatePerDay(dto.getRatePerDay());
		bike.setFuelType(FuelType.valueOf(dto.getFuelType().toUpperCase()));
		bike.setStatus(BikeStatus.valueOf(dto.getStatus().toUpperCase()));

		return mapToResponse(bike);
	}

	@Override
	public BikeResponseDTO getBikeById(Long bikeId) {

		Bike bike = bikeRepository.findByIdAndIsDeletedFalse(bikeId)
				.orElseThrow(() -> new BikeNotFoundException("Bike not found"));

		return mapToResponse(bike);
	}

	@Override
	public List<BikeResponseDTO> getAllBikes() {

		return bikeRepository.findAllByIsDeletedFalse()
				.stream()
				.map(this::mapToResponse)
				.toList();
	}

	@Override
	public void deleteBike(Long bikeId) {

		Bike bike = bikeRepository.findByIdAndIsDeletedFalse(bikeId)
				.orElseThrow(() -> new BikeNotFoundException("Bike not found"));

		bike.setIsDeleted(true);
		bikeRepository.save(bike);
	}

	private BikeResponseDTO mapToResponse(Bike bike) {
		return new BikeResponseDTO(
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
		);
	}
}
