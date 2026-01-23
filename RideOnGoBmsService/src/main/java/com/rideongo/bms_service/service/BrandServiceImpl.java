package com.rideongo.bms_service.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rideongo.bms_service.custom_exceptions.DuplicateBrandException;
import com.rideongo.bms_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.bms_service.dtos.BrandRequestDTO;
import com.rideongo.bms_service.dtos.BrandResponseDTO;
import com.rideongo.bms_service.entities.Brand;
import com.rideongo.bms_service.repository.BrandRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

	private final BrandRepository brandRepository;
	private final ModelMapper modelMapper;

	@Override
	public BrandResponseDTO createBrand(BrandRequestDTO dto) {

		if (brandRepository.existsByBrandNameAndIsDeletedFalse(dto.getBrandName())) {
			throw new DuplicateBrandException("Brand already exists");
		}

		Brand brand = modelMapper.map(dto, Brand.class);
		Brand saved = brandRepository.save(brand);

		return new BrandResponseDTO(saved.getId(), saved.getBrandName());
	}

	@Override
	public BrandResponseDTO updateBrand(Long brandId, BrandRequestDTO dto) {
		
		Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

		brand.setBrandName(dto.getBrandName());

		return new BrandResponseDTO(brand.getId(), brand.getBrandName());
	}

	@Override
	public BrandResponseDTO getBrandById(Long brandId) {

		Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

		return new BrandResponseDTO(brand.getId(), brand.getBrandName());
	}

	@Override
	public List<BrandResponseDTO> getAllBrands() {

		return brandRepository.findAllByIsDeletedFalse()
				.stream()
				.map(b -> new BrandResponseDTO(b.getId(), b.getBrandName()))
				.toList();
	}

	@Override
	public void deleteBrand(Long brandId) {

		Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

		brand.setIsDeleted(true);
		brandRepository.save(brand);
	}
}
