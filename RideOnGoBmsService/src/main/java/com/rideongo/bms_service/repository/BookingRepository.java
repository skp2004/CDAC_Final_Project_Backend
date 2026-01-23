package com.rideongo.bms_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideongo.bms_service.entities.Booking;
import com.rideongo.bms_service.entities.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByIdAndIsDeletedFalse(Long id);

	List<Booking> findAllByIsDeletedFalse();

	List<Booking> findByUserIdAndIsDeletedFalse(Long userId);

	List<Booking> findByBookingStatusAndIsDeletedFalse(BookingStatus status);
}
