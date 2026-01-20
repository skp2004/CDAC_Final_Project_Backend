package com.rideongo.ums_service.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rideongo.ums_service.entities.User;
import com.rideongo.ums_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("********* in load user ");
		User user=userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User by this email doesn't exist!!!!!!!!"));
		//email verified
		return new UserPrincipal(user.getId(),
				user.getEmail(),user.getPassword(),
				List.of(new SimpleGrantedAuthority(user.getUserRole().name())),user.getUserRole().name());
	}

}
