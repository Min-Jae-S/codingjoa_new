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

import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("## {}", this.getClass().getSimpleName());
		
		/*
		 * ref) UsernamePasswordAuthenticationFilter#attemptAuthentication
		 * public Authentication attemptAuthentication(HttpServletRequest request,
				HttpServletResponse response) throws AuthenticationException {
		 * 
		 * 		String username = obtainUsername(request);
		 * 		String password = obtainPassword(request);
		 * 
		 * 		if (username == null) { username = ""; }
		 * 		if (password == null) { password = ""; }
		 * 		username = username.trim()
		 * 
		 * 		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
		 * 				username, password);
		 * 		...
		 * 
		 *  	return this.getAuthenticationManager().authenticate(authRequest);
		 *  }
		 *
		 */
		
		String memberId = (String) authentication.getPrincipal();
		String memberPassword = (String) authentication.getCredentials();
		log.info("\t > memberId = '{}'", memberId);
		log.info("\t > memberPassword = '{}'", memberPassword);
		
		if ("".equals(memberId)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequireId"));
		} else if ("".equals(memberPassword)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequirePassword"));
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		if (!passwordEncoder.matches(memberPassword, userDetails.getPassword())) {
			throw new BadCredentialsException(
					MessageUtils.getMessage("error.UsernameNotFoundOrBadCredentials"));
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
