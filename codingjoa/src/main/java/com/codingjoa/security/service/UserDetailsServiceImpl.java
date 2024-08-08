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
import com.codingjoa.security.dto.UserDetailsDto;
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
		Map<String, Object> userDetails = memberMapper.findUserDetailsById(memberId);
		
		if (userDetails == null) {
			throw new UsernameNotFoundException(MessageUtils.getMessage("error.UsernameNotFoundOrBadCredentials"));
		}
		
		return modelMapper.map(userDetails, UserDetailsDto.class);
	}
}
