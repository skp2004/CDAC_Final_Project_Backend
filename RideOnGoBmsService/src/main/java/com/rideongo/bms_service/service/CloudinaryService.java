package com.rideongo.bms_service.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

	private final Cloudinary cloudinary;

	public String uploadImage(MultipartFile file, String folder) throws IOException {

		Map<?, ?> uploadResult = cloudinary.uploader().upload(
				file.getBytes(),
				ObjectUtils.asMap("folder", folder)
		);

		return uploadResult.get("secure_url").toString();
	}
}

