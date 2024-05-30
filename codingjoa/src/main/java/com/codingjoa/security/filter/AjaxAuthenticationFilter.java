package com.codingjoa.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final String USERNAME_KEY = "memberId";
	private static final String PASSWORD_KEY = "memberPassword";
	private ObjectMapper objectMapper = new ObjectMapper();

	public AjaxAuthenticationFilter() {
		super(new AntPathRequestMatcher("/api/login", "POST"));
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		Map<String, String> map = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, String>>() {});
		String memberId = map.get(USERNAME_KEY);
		String memberPassword = map.get(PASSWORD_KEY);
		log.info("\t > memberId = '{}', memberPassword = '{}'", memberId, memberPassword);
		
		if (!StringUtils.hasText(memberId)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequireId"));	
		}
		
		if (!StringUtils.hasText(memberPassword)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequirePassword"));
		}
		
		//memberId = memberId.trim();
		
		UsernamePasswordAuthenticationToken authRequest = 
				new UsernamePasswordAuthenticationToken(memberId, memberPassword);
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
