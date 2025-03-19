package com.codingjoa.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.UserService;
import com.codingjoa.util.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("## {}.loadUserByUsername", this.getClass().getSimpleName());
		
		PrincipalDetails principalDetails = userService.getUserDetailsByEmail(username);
		
		if (principalDetails == null) {
			throw new UsernameNotFoundException(MessageUtils.getMessage("error.badCredential"));
		}
		
		return principalDetails;
	}
	
}