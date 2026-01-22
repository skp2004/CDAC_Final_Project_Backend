package com.rideongo.ums_service.entities;

import java.time.LocalDate;

//JPA
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity 
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = { "password", "image" }, callSuper = true)

public class User extends BaseEntity {

	@Column(name = "first_name", length = 30) // varchar(30)
	private String firstName;
	@Column(name = "last_name", length = 30)
	private String lastName;
	@Column(unique = true, length = 50)
	private String email;

	@Column(nullable = false)
	private String password;

	private LocalDate dob;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;
	@Column(unique = true, length = 14)
	private String phone;
	@Column(name = "image_url", length = 500)
	private String image;

	@Column(name = "is_verified", nullable = false)
	private boolean isVerified = false;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@Column(name = "kyc_status", length = 20)
	private String kycStatus;
	@Column(name = "aadhaar_url", length = 500)
	private String aadhaarUrl; // Only populated for CUSTOMER role
 
	@Column(name = "licence_url", length = 500)
	private String licenceUrl;

	public User(String firstName, String lastName, String email, String password, LocalDate dob, String phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.dob = dob;
		this.phone = phone;
	}

}
