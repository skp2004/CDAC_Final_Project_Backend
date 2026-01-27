package com.rideongo.bms_service.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@AttributeOverride(name = "id", column = @Column(name = "review_id"))
@Getter
@Setter
@NoArgsConstructor
public class Review extends BaseEntity {

	@Column(name = "rating", nullable = false)
	private Integer rating;

	@Column(name = "comments", length = 500)
	private String comments;

	// Reference to Bike (unidirectional, no relationship mapping)
	@Column(name = "bike_id", nullable = false)
	private Long bikeId;

	// Reference to User from UMS
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;
}
