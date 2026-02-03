package com.rideongo.bms_service.entities;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bookings")
@AttributeOverride(name = "id", column = @Column(name = "booking_id"))
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Booking extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "bike_id", nullable = false)
	private Bike bike;

	@Column(name = "user_id", nullable = false)
	private Long userId; // From UMS (NO relationship)

	@Column(name = "pickup_ts", nullable = false)
	private LocalDateTime pickupTs;

	@Column(name = "drop_ts", nullable = false)
	private LocalDateTime dropTs;

	@Enumerated(EnumType.STRING)
	@Column(name = "rental_type", length = 20, nullable = false)
	private RentalType rentalType;

	// Pickup Type: STATION or DOORSTEP
	@Enumerated(EnumType.STRING)
	@Column(name = "pickup_type", length = 20, nullable = false)
	private PickupType pickupType = PickupType.STATION;

	// For STATION pickup - reference to location
	@ManyToOne
	@JoinColumn(name = "pickup_location_id")
	private Location pickupLocation;

	// For DOORSTEP delivery - customer's address
	@Column(name = "delivery_address", length = 500)
	private String deliveryAddress;

	@Column(name = "tax_amount", nullable = false)
	private Double taxAmount;

	@Column(name = "discount_amount", nullable = false)
	private Double discountAmount;

	@Column(name = "total_amount", nullable = false)
	private Double totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status", length = 20, nullable = false)
	private BookingStatus bookingStatus;
}
