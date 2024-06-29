package com.codingjoa.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.codingjoa.security.dto.LoginDto;
import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public JwtAuthenticationFilter() {
		super(new AntPathRequestMatcher("/api/login", "POST"));
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.info("## {}.attemptAuthentication", this.getClass().getSimpleName());
		
		// check ajax and POST 
		
		LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
		log.info("\t > {}", loginDto);
		
		String memberId = loginDto.getMemberId();
		String memberPassword = loginDto.getMemberPassword();
		
		if (!StringUtils.hasText(memberId)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequireId"));	
		}
		
		if (!StringUtils.hasText(memberPassword)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequirePassword"));
		}
		
		memberId = memberId.trim();
		
		UsernamePasswordAuthenticationToken authRequest = 
				new UsernamePasswordAuthenticationToken(memberId, memberPassword); // isAuthentiacated = false

		setDetails(request, authRequest);
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("## {}.successfulAuthentication", this.getClass().getSimpleName());
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}
	
}
