package com.rideongo.bms_service.service;

import java.util.List;

import com.rideongo.bms_service.dtos.BookingRequestDTO;
import com.rideongo.bms_service.dtos.BookingResponseDTO;

public interface BookingService {

	BookingResponseDTO createBooking(BookingRequestDTO dto);

	BookingResponseDTO getBookingById(Long bookingId);

	List<BookingResponseDTO> getAllBookings();

	List<BookingResponseDTO> getBookingsByUser(Long userId);

	void cancelBooking(Long bookingId);
}
