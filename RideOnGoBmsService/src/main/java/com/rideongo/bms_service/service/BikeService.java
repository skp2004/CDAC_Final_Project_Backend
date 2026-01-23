package com.rideongo.bms_service.service;


import java.util.List;

import com.rideongo.bms_service.dtos.BikeRequestDTO;
import com.rideongo.bms_service.dtos.BikeResponseDTO;

public interface BikeService {

	BikeResponseDTO createBike(BikeRequestDTO dto);

	BikeResponseDTO updateBike(Long bikeId, BikeRequestDTO dto);

	BikeResponseDTO getBikeById(Long bikeId);

	List<BikeResponseDTO> getAllBikes();

	void deleteBike(Long bikeId);
}
