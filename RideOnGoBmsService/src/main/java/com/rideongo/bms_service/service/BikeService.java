package com.rideongo.bms_service.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rideongo.bms_service.dtos.BikeRequestDTO;
import com.rideongo.bms_service.dtos.BikeResponseDTO;

public interface BikeService {

	BikeResponseDTO createBike(
			BikeRequestDTO dto,
			MultipartFile image  
	) throws IOException;

	BikeResponseDTO updateBike(Long bikeId, BikeRequestDTO dto);

	BikeResponseDTO getBikeById(Long bikeId);

	List<BikeResponseDTO> getAllBikes();

	void deleteBike(Long bikeId);
	List<BikeResponseDTO> getBikesByLocationId(Long locationId);

	void updateBikeStatus(Long bikeId, String status); 
}
