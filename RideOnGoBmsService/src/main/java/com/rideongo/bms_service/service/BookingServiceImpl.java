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

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final BikeRepository bikeRepository;

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

	private BookingResponseDTO mapToResponse(Booking booking) {
		return new BookingResponseDTO(
				booking.getId(),
				booking.getBike().getId(),
				booking.getUserId(),
				booking.getPickupTs(),
				booking.getDropTs(),
				booking.getRentalType().name(),
				booking.getTaxAmount(),
				booking.getDiscountAmount(),
				booking.getTotalAmount(),
				booking.getBookingStatus().name()
		);
	}
}
