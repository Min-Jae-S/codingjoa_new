package com.codingjoa.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.codingjoa.security.exception.LoginRequireFieldException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("============== CustomAuthenticationProvider ==============");
		
		String memberId = (String) authentication.getPrincipal();
		String memberPassword = (String) authentication.getCredentials();
		
		if ("".equals(memberId)) {
			throw new LoginRequireFieldException("error.LoginRequireField.memberId");
		} else if(!StringUtils.hasText(memberPassword)) {
			throw new LoginRequireFieldException("error.LoginRequireField.memberPassword");
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		log.info("{}", userDetails);
		
		if (!passwordEncoder.matches(memberPassword, userDetails.getPassword())) {
			throw new BadCredentialsException("error.UsernameNotFoundOrBadCredentials");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
