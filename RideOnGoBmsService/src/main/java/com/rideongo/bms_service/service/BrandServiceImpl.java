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
import com.rideongo.bms_service.repository.BikeRepository;
import com.rideongo.bms_service.repository.BrandRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

	private final BrandRepository brandRepository;
	private final ModelMapper modelMapper;
	private final BikeRepository bikeRepository;


	@Override
	public BrandResponseDTO createBrand(BrandRequestDTO dto) {

		if (brandRepository.existsByBrandNameAndIsDeletedFalse(dto.getBrandName())) {
			throw new DuplicateBrandException("Brand already exists");
		}

		Brand brand = modelMapper.map(dto, Brand.class);
		Brand saved = brandRepository.save(brand);
		long total = bikeRepository.countByBrand_Id(brand.getId());
		

		return new BrandResponseDTO(saved.getId(), saved.getBrandName(), saved.getIsActive(),total);
	}

	@Override
	public BrandResponseDTO updateBrand(Long brandId, BrandRequestDTO dto) {
		
		Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

		brand.setBrandName(dto.getBrandName());
		long total = bikeRepository.countByBrand_Id(brand.getId());

		return new BrandResponseDTO(brand.getId(), brand.getBrandName(), brand.getIsActive(),total);
	}

	@Override
    public BrandResponseDTO getBrandById(Long brandId) {

        Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        long total = bikeRepository.countByBrand_Id(brandId);

        return new BrandResponseDTO(
                brand.getId(),
                brand.getBrandName(),
                brand.getIsActive(),
                total
        );
    }

    @Override
    public List<BrandResponseDTO> getAllBrands() {

        return brandRepository.findAllByIsDeletedFalse()
                .stream()
                .map(b -> new BrandResponseDTO(
                        b.getId(),
                        b.getBrandName(),
                        b.getIsActive(),
                        bikeRepository.countByBrand_Id(b.getId())
                ))
                .toList();
    }
	@Override
	public void deleteBrand(Long brandId) {

		Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

		brand.setIsDeleted(true);
		brandRepository.save(brand);
	}
	@Override
	public void toggleBrandStatus(Long brandId, Boolean isActive) {

		Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

		brand.setIsActive(isActive);
	}
	
	@Override
	public List<BrandResponseDTO> getActiveBrands() {

		return brandRepository.findAllByIsDeletedFalseAndIsActiveTrue()
				.stream()
				.map(this::mapToResponse)
				.toList();
	}
	
	private BrandResponseDTO mapToResponse(Brand brand) {
		return new BrandResponseDTO(
				brand.getId(),
				brand.getBrandName(),
				brand.getIsActive(),
				bikeRepository.countByBrand_Id(brand.getId())
		);
	}
}
