package com.codingjoa.security.service;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.codingjoa.entity.ProfileImage;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Component
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
		log.info("\t > find userDetailsMap by '{}' = {}", memberId, userDetailsMap);
		
		if (userDetailsMap == null) {
			throw new UsernameNotFoundException(
					MessageUtils.getMessage("error.UsernameNotFoundOrBadCredentials"));
		}
		log.info("\t\t - profileImage = {}", userDetailsMap.get("profileImage"));
		
		ProfileImage profileImage = (ProfileImage) userDetailsMap.get("profileImage");
		if (profileImage.getProfileImageIdx() == null) {
			userDetailsMap.replace("profileImage", null);
		}
		log.info("\t > post-processing on userDetailsMap = {}", userDetailsMap);
		log.info("\t\t - profileImage = {}", userDetailsMap.get("profileImage"));
		
		log.info("\t > return UserDetailsDto converted from the userDetailsMap");
		return modelMapper.map(userDetailsMap, UserDetailsDto.class);
	}
}
