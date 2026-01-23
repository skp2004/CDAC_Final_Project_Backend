package com.rideongo.bms_service.service;


import java.util.List;

import com.rideongo.bms_service.dtos.BrandRequestDTO;
import com.rideongo.bms_service.dtos.BrandResponseDTO;

public interface BrandService {

	BrandResponseDTO createBrand(BrandRequestDTO dto);

	BrandResponseDTO updateBrand(Long brandId, BrandRequestDTO dto);

	BrandResponseDTO getBrandById(Long brandId);

	List<BrandResponseDTO> getAllBrands();

	void deleteBrand(Long brandId);
}
