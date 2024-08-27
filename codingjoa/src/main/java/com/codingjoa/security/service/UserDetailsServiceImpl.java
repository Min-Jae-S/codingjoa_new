package com.codingjoa.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.MemberService;
import com.codingjoa.util.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberService memberService;
	
	@Override
	public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
		log.info("## {}.loadUserByUsername", this.getClass().getSimpleName());
		
		PrincipalDetails principalDetails = memberService.getUserDetailsByEmail(memberEmail);
		
		if (principalDetails == null) {
			throw new UsernameNotFoundException(MessageUtils.getMessage("error.BadCredential"));
		}
		
		return principalDetails;
	}
	
}