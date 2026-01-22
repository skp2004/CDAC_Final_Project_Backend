package com.rideongo.ums_service.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
	private final Cloudinary cloudinary;
	// Supported image formats
    private static final List<String> SUPPORTED_IMAGE_FORMATS = Arrays.asList(
            "image/jpeg",
            "image/jpg", 
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp",
            "image/svg+xml"
    );
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Validate file type
        validateImageFile(file);

        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "auto",
                    "public_id", UUID.randomUUID().toString(),
                    "format", extractFileExtension(file.getOriginalFilename()),
                    "allowed_formats", Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg")
            );

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            String imageUrl = (String) uploadResult.get("secure_url");
            
            log.info("Image uploaded successfully to Cloudinary: {}", imageUrl);
            return imageUrl;
            
        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary", e);
            throw new IOException("Failed to upload image: " + e.getMessage());
        }
    }

    private void validateImageFile(MultipartFile file) throws IOException {
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds maximum limit of 10MB");
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !SUPPORTED_IMAGE_FORMATS.contains(contentType.toLowerCase())) {
            throw new IOException(
                "Invalid file format. Supported formats: JPG, JPEG, PNG, GIF, BMP, WEBP, SVG. Got: " + contentType
            );
        }

        // Additional check for file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = extractFileExtension(originalFilename);
            if (extension == null || !isValidExtension(extension)) {
                throw new IOException(
                    "Invalid file extension. Supported extensions: jpg, jpeg, png, gif, bmp, webp, svg"
                );
            }
        }

        log.info("File validation passed for: {} (Type: {}, Size: {} bytes)", 
                 originalFilename, contentType, file.getSize());
    }

    private String extractFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isValidExtension(String extension) {
        return Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg")
                .contains(extension.toLowerCase());
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Image deleted successfully from Cloudinary: {}", publicId);
            }
        } catch (Exception e) {
            log.error("Error deleting image from Cloudinary", e);
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/");
            String fileNameWithExtension = parts[parts.length - 1];
            String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
            String folder = parts[parts.length - 2];
            return folder + "/" + fileName;
        } catch (Exception e) {
            log.error("Error extracting public ID from URL: {}", imageUrl, e);
            return null;
        }
    }
}
