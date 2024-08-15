package com.codingjoa.security.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		log.info("## {}.loadUserByUsername", this.getClass().getSimpleName());
		
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsById(memberId);

		if (userDetailsMap == null) {
			throw new UsernameNotFoundException(MessageUtils.getMessage("error.UsernameNotFoundOrBadCredentials"));
		}
		
		PrincipalDetails principalDetails = PrincipalDetails.from(userDetailsMap);
		log.info("{}", Utils.formatPrettyJson(principalDetails));

		return principalDetails;
	}
}
