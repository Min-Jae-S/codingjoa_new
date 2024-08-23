package com.codingjoa.security.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codingjoa.util.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginProvider implements AuthenticationProvider { // AbstractUserDetailsAuthenticationProvider (DaoAuthenticationProvider)
	
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > starting authentication of the {}", authentication.getClass().getSimpleName());
		
		UsernamePasswordAuthenticationToken loginToken = (UsernamePasswordAuthenticationToken) authentication;
		String memberEmail = loginToken.getName();
		String memberPassword = (String) loginToken.getCredentials();
		
		UserDetails loadedUser = userDetailsService.loadUserByUsername(memberEmail); // PrincipalDetails
		
		if (!passwordEncoder.matches(memberPassword, loadedUser.getPassword())) {
			throw new BadCredentialsException(MessageUtils.getMessage("error.InvalidCredential"));
		}
		
		UsernamePasswordAuthenticationToken authenticatedLoginToken = new UsernamePasswordAuthenticationToken(
				loadedUser, null, loadedUser.getAuthorities());
		
		return authenticatedLoginToken;
	}

}
