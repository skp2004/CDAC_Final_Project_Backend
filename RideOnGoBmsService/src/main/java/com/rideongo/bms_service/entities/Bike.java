package com.rideongo.bms_service.entities;


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
@Table(name = "bikes")
@AttributeOverride(name = "id", column = @Column(name = "bike_id"))
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Bike extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;

	@Column(name = "cc", nullable = false)
	private Long cc;

	@Column(name = "colour", length = 30, nullable = false)
	private String colour;

	@Column(name = "mileage", nullable = false)
	private Long mileage;

	@Column(name = "rate_per_hour", nullable = false)
	private Double ratePerHour;

	@Column(name = "rate_per_day", nullable = false)
	private Double ratePerDay;

	@Enumerated(EnumType.STRING)
	@Column(name = "fuel_type", length = 20, nullable = false)
	private FuelType fuelType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20, nullable = false)
	private BikeStatus status;
	
	@ManyToOne(optional = false)          
	@JoinColumn(name = "location_id", nullable = false) 
	private Location location; 
	

	@Column(name = "image_url", length = 500) 
	private String image;
	
	@Column(name = "description", length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20, nullable = false)
    private BikeCategory category;

    @Column(name = "price_per_7_days", nullable = false)
    private Double pricePer7Days;
}
