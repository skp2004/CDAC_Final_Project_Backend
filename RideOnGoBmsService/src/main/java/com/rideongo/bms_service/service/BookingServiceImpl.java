package com.rideongo.bms_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rideongo.bms_service.custom_exceptions.BookingNotFoundException;
import com.rideongo.bms_service.custom_exceptions.ResourceNotFoundException;
import com.rideongo.bms_service.dtos.BookingRequestDTO;
import com.rideongo.bms_service.dtos.BookingResponseDTO;
import com.rideongo.bms_service.entities.*;
import com.rideongo.bms_service.repository.BikeRepository;
import com.rideongo.bms_service.repository.BookingRepository;
import com.rideongo.bms_service.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final BikeRepository bikeRepository;
	private final LocationRepository locationRepository;

	@Override
	public BookingResponseDTO createBooking(BookingRequestDTO dto) {

		Bike bike = bikeRepository.findByIdAndIsDeletedFalse(dto.getBikeId())
				.orElseThrow(() -> new ResourceNotFoundException("Bike not found"));

		Booking booking = new Booking();
		booking.setBike(bike);
		booking.setUserId(dto.getUserId());
		booking.setPickupTs(dto.getPickupTs());
		booking.setDropTs(dto.getDropTs());
		booking.setRentalType(RentalType.valueOf(dto.getRentalType().toUpperCase()));

		// Handle pickup type and location/address
		PickupType pickupType = PickupType.valueOf(dto.getPickupType().toUpperCase());
		booking.setPickupType(pickupType);

		if (pickupType == PickupType.STATION && dto.getPickupLocationId() != null) {
			Location location = locationRepository.findById(dto.getPickupLocationId())
					.orElseThrow(() -> new ResourceNotFoundException("Pickup location not found"));
			booking.setPickupLocation(location);
		}

		if (pickupType == PickupType.DOORSTEP && dto.getDeliveryAddress() != null) {
			booking.setDeliveryAddress(dto.getDeliveryAddress());
		}

		booking.setTaxAmount(dto.getTaxAmount());
		booking.setDiscountAmount(dto.getDiscountAmount());
		booking.setTotalAmount(dto.getTotalAmount());
		booking.setBookingStatus(BookingStatus.CONFIRMED);

		Booking saved = bookingRepository.save(booking);

		return mapToResponse(saved);
	}

	@Override
	public BookingResponseDTO getBookingById(Long bookingId) {

		Booking booking = bookingRepository.findByIdAndIsDeletedFalse(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		return mapToResponse(booking);
	}

	@Override
	public List<BookingResponseDTO> getAllBookings() {

		return bookingRepository.findAllByIsDeletedFalse()
				.stream()
				.map(this::mapToResponse)
				.toList();
	}

	@Override
	public List<BookingResponseDTO> getBookingsByUser(Long userId) {

		return bookingRepository.findByUserIdAndIsDeletedFalse(userId)
				.stream()
				.map(this::mapToResponse)
				.toList();
	}

	@Override
	public void cancelBooking(Long bookingId) {

		Booking booking = bookingRepository.findByIdAndIsDeletedFalse(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		booking.setBookingStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
	}

	@Override
	public BookingResponseDTO updateBookingStatus(Long bookingId, String status) {
		Booking booking = bookingRepository.findByIdAndIsDeletedFalse(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		try {
			booking.setBookingStatus(BookingStatus.valueOf(status.toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid booking status: " + status);
		}

		Booking saved = bookingRepository.save(booking);

		return mapToResponse(saved);
	}

	private BookingResponseDTO mapToResponse(Booking booking) {
		Bike bike = booking.getBike();
		String bikeName = bike.getBrand().getBrandName() + " " + bike.getCategory().name();

		BookingResponseDTO dto = new BookingResponseDTO();
		dto.setBookingId(booking.getId());
		dto.setBikeId(bike.getId());
		dto.setUserId(booking.getUserId());
		dto.setPickupTs(booking.getPickupTs());
		dto.setDropTs(booking.getDropTs());
		dto.setRentalType(booking.getRentalType().name());
		dto.setTaxAmount(booking.getTaxAmount());
		dto.setDiscountAmount(booking.getDiscountAmount());
		dto.setTotalAmount(booking.getTotalAmount());
		dto.setBookingStatus(booking.getBookingStatus().name());

		// Bike info
		dto.setBikeName(bikeName);
		dto.setBikeImage(bike.getImage());

		// Pickup type
		dto.setPickupType(booking.getPickupType() != null ? booking.getPickupType().name() : "STATION");
		dto.setCreatedAt(booking.getCreatedAt());

		// Location details for STATION pickup
		Location location = booking.getPickupLocation();
		if (location != null) {
			dto.setPickupLocationId(location.getId());
			dto.setPickupLocationAddress(location.getAddress());
			dto.setPickupLocationCity(location.getCity());
			dto.setPickupLocationState(location.getState());
			dto.setPickupLocationPincode(location.getPincode());
			dto.setPickupLocationContact(location.getContactNumber());
		}

		// Delivery address for DOORSTEP
		dto.setDeliveryAddress(booking.getDeliveryAddress());

		return dto;
	}
}
