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
		booking.setPickupType(PickupType.valueOf(dto.getPickupType().toUpperCase()));

		// Handle pickup location or delivery address based on pickup type
		if (booking.getPickupType() == PickupType.STATION && dto.getPickupLocationId() != null) {
			Location pickupLocation = locationRepository.findById(dto.getPickupLocationId())
					.orElseThrow(() -> new ResourceNotFoundException("Pickup location not found"));
			booking.setPickupLocation(pickupLocation);
		} else if (booking.getPickupType() == PickupType.DOORSTEP) {
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
		booking.setIsDeleted(true);
		bookingRepository.save(booking);
	}

	@Override
	public BookingResponseDTO updateBookingStatus(Long bookingId, String status) {

		Booking booking = bookingRepository.findByIdAndIsDeletedFalse(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		booking.setBookingStatus(BookingStatus.valueOf(status.toUpperCase()));
		Booking saved = bookingRepository.save(booking);

		return mapToResponse(saved);
	}

	@Override
	public BookingResponseDTO updateRazorpayOrderId(Long bookingId, String razorpayOrderId) {

		Booking booking = bookingRepository.findByIdAndIsDeletedFalse(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		booking.setRazorpayOrderId(razorpayOrderId);
		Booking saved = bookingRepository.save(booking);

		return mapToResponse(saved);
	}

	private BookingResponseDTO mapToResponse(Booking booking) {
		return BookingResponseDTO.builder()
				.bookingId(booking.getId())
				.bikeId(booking.getBike().getId())
				.bikeName(booking.getBike().getBrand().getBrandName())
				.bikeImage(booking.getBike().getImage())
				.userId(booking.getUserId())
				.pickupTs(booking.getPickupTs())
				.dropTs(booking.getDropTs())
				.rentalType(booking.getRentalType().name())
				.pickupType(booking.getPickupType().name())
				.pickupLocationId(booking.getPickupLocation() != null ? booking.getPickupLocation().getId() : null)
				.pickupLocationAddress(
						booking.getPickupLocation() != null ? booking.getPickupLocation().getAddress() : null)
				.pickupLocationCity(booking.getPickupLocation() != null ? booking.getPickupLocation().getCity() : null)
				.deliveryAddress(booking.getDeliveryAddress())
				.taxAmount(booking.getTaxAmount())
				.discountAmount(booking.getDiscountAmount())
				.totalAmount(booking.getTotalAmount())
				.bookingStatus(booking.getBookingStatus().name())
				.razorpayOrderId(booking.getRazorpayOrderId())
				.createdAt(booking.getCreatedAt())
				.build();
	}
}
