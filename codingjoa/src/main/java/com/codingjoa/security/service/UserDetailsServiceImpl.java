package com.codingjoa.security.service;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		log.info("## {}", this.getClass().getSimpleName());
		
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsById(memberId);
		log.info("\t > find userDetailsMap by memberId");
		log.info("\t > userDetailsMap = {}", userDetailsMap);
		
		if (userDetailsMap == null) {
			throw new UsernameNotFoundException(
					MessageUtils.getMessage("error.UsernameNotFoundOrBadCredentials"));
		}
		
		log.info("\t > convert userDetailsMap to UserDetailsDto");
		return modelMapper.map(userDetailsMap, UserDetailsDto.class);
	}
}
