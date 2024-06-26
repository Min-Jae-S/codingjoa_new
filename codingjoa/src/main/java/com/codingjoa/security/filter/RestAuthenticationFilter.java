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
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public RestAuthenticationFilter() {
		super(new AntPathRequestMatcher("/api/login", "POST"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		// check ajax and POST 
		
		Map<String, Object> loginMap = objectMapper.readValue(request.getReader(), Map.class);
		log.info("\t > loginMap = {}", loginMap);
		
		String memberId = (String) loginMap.get("memberId");
		String memberPassword = (String) loginMap.get("memberPassword");
		
		if (!StringUtils.hasText(memberId)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequireId"));	
		}
		
		if (!StringUtils.hasText(memberPassword)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequirePassword"));
		}
		
		memberId = memberId.trim();
		
		UsernamePasswordAuthenticationToken authRequest = 
				new UsernamePasswordAuthenticationToken(memberId, memberPassword); // isAuthentiacated = false
		
		String redirectUrl = (String) loginMap.get("redirectUrl");
		authRequest.setDetails(redirectUrl);
		
		log.info("\t > authentication token = {} [ {} ]", 
				authRequest.getClass().getSimpleName() + "@" + authRequest.getClass().hashCode(), 
				(authRequest.isAuthenticated() == true) ? "authenticated" : "not authenticated");
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}
	
	protected void setDetails(String redirectUrl, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(redirectUrl);
	}
	
}
