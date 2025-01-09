package com.codingjoa.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.codingjoa.security.dto.LoginDto;
import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.AjaxUtils;
import com.codingjoa.util.FormatUtils;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter { // UsernamePasswordAuthenticationFilter
	
	public static final String DEFAULT_FILTER_PROCESSES_URL= "/api/login";
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public LoginFilter() {
		super(new AntPathRequestMatcher(DEFAULT_FILTER_PROCESSES_URL, "POST"));
	}
	
	public LoginFilter(String filterProcessesUrl) {
		super(new AntPathRequestMatcher(filterProcessesUrl, "POST"));
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("## {}.attemptAuthentication", this.getClass().getSimpleName());
		
		if (!AjaxUtils.isAjaxRequest(request)) {
			throw new AuthenticationServiceException("invalid request");
		}
		
		LoginDto loginDto;
		try {
			loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
			log.info("\t > loginDto = {}", loginDto);
		} catch (IOException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
			throw new AuthenticationServiceException("invalid json format");
		}
		
		String memberEmail = loginDto.getMemberEmail();
		String memberPassword = loginDto.getMemberPassword();
		
		if (!StringUtils.hasText(memberEmail)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequireEmail"));
		}
		
		if (!StringUtils.hasText(memberPassword)) {
			throw new LoginRequireFieldException(MessageUtils.getMessage("error.LoginRequirePassword"));
		}
		
		memberEmail = memberEmail.trim();
		
		UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(memberEmail, memberPassword);
		
		// authenticate UsernamePasswordAuthenticationToken by LoginProvider
		UsernamePasswordAuthenticationToken authenticatedLoginToken = 
				(UsernamePasswordAuthenticationToken) this.getAuthenticationManager().authenticate(loginToken);
		
		String continueParameter = request.getParameter("continue");
		authenticatedLoginToken.setDetails(continueParameter);
		log.info("## set the continueUrl in details: {}", FormatUtils.formatString(continueParameter));
		
		return authenticatedLoginToken;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//SecurityContextHolder.getContext().setAuthentication(authResult)
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
}
