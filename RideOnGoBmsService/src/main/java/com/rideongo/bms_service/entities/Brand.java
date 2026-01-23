package com.rideongo.bms_service.entities;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "brands")
@AttributeOverride(name = "id", column = @Column(name = "brand_id"))
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Brand extends BaseEntity {

	@Column(name = "brand_name", nullable = false, length = 50)
	private String brandName;
}
