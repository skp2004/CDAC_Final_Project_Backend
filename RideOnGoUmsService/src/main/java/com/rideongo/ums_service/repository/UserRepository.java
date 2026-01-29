package com.rideongo.ums_service.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rideongo.ums_service.dtos.UserDTO;
import com.rideongo.ums_service.entities.User;
import com.rideongo.ums_service.entities.UserRole;


public interface UserRepository extends JpaRepository<User, Long> {


	/*
	 * Return count of users born after specified date
	 */
	int countByDobAfter(LocalDate date);

	/*
	 * Delete all users from specified role
	 */
	int deleteByUserRole(UserRole role);

	/*
	 * Get firstname , last name , dob of users from specified role , born
	 * between specified start & end date
	 */
	@Query("""
		    select new com.rideongo.ums_service.dtos.UserDTO(
		        u.id,
		        u.firstName,
		        u.lastName,
		        u.dob,
		        u.userRole,
		        u.phone,
		        u.image,
		        u.isVerified
		    )
		    from User u
		    where u.userRole = :rl
		      and u.dob between :start and :end
		""")
		List<UserDTO> getSelectedUserDetails(
		        @Param("rl") UserRole role,
		        @Param("start") LocalDate start,
		        @Param("end") LocalDate end
		);

	
	 //check if user already exists by same email or phone
	 
	boolean existsByEmailOrPhone(String email,String phoneNo);
	// sign in
	
	Optional<User> findByEmailAndPassword(String email, String password);

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);
}
