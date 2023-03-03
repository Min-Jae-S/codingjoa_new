package com.codingjoa.security.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.codingjoa.entity.Member;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.UserDetailsDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		log.info("loadUserByUsername, memberId = {}", memberId);
		
		Map<String, Object> map = memberMapper.findUserDetailsById(memberId);
		log.info("{}", map);
		
		if(map == null) {
			throw new UsernameNotFoundException("error.UsernameNotFoundOrBadCredentials");
		}
		
		Member member = (Member) map.get("member"); 			
		String memberRole = (String) map.get("memberRole");
			
		return new UserDetailsDto(member, memberRole);
	}
}
