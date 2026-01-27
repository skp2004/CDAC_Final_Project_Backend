package com.rideongo.bms_service.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseEntity {

	@Column(name = "booking_id", nullable = false)
	private Long bookingId;

	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "payment_mode", nullable = false, length = 20)
	private String paymentMode; // UPI / CARD / CASH

	@Column(name = "payment_type", nullable = false, length = 20)
	private String paymentType; // FULL / PARTIAL / REFUND

	@Column(name = "txn_ref", nullable = false, unique = true, length = 50)
	private String txnRef;

	@Column(name = "status", nullable = false, length = 20)
	private String status; // SUCCESS / FAILED / PENDING

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;
}
