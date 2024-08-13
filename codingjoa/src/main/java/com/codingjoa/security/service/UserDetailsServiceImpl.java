package com.codingjoa.security.service;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberMapper memberMapper;
	private final ModelMapper modelMapper;
	
	@Autowired
	public UserDetailsServiceImpl(MemberMapper memberMapper, @Qualifier("customModelMapper") ModelMapper modelMapper) {
		this.memberMapper = memberMapper;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		log.info("## {}.loadUserByUsername", this.getClass().getSimpleName());
		
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsById(memberId);

		if (userDetailsMap == null) {
			throw new UsernameNotFoundException(MessageUtils.getMessage("error.UsernameNotFoundOrBadCredentials"));
		}
		
		PrincipalDetails principalDetails = modelMapper.map(userDetailsMap, PrincipalDetails.class);
		//log.info("\t > principalDetails = {}", Utils.formatJson(principalDetails));

		return principalDetails;
	}
}
