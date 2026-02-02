package com.rideongo.bms_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideongo.bms_service.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	boolean existsByTxnRef(String txnRef);

	List<Payment> findByBookingIdAndIsDeletedFalse(Long bookingId);

	List<Payment> findAllByIsDeletedFalse();
}
