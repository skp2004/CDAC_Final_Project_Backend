package com.rideongo.bms_service.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locations")
@AttributeOverride(name = "id", column = @Column(name = "location_id"))
@Getter
@Setter
@NoArgsConstructor
public class Location extends BaseEntity {

	@Column(name = "address", nullable = false, length = 255)
	private String address;

	@Column(name = "city", nullable = false, length = 50)
	private String city;

	@Column(name = "state", nullable = false, length = 50)
	private String state;

	@Column(name = "pincode", nullable = false, length = 10)
	private String pincode;

	@Column(name = "contact_number", nullable = false, length = 15)
	private String contactNumber;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;
}
