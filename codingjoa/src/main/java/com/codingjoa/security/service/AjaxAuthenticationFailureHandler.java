package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("\t > authentication token = {}",(authentication != null) ? authentication.getClass().getSimpleName() : authentication);
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.timeZone(TimeZone.getTimeZone("Asia/Seoul"))
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
				.build();
		
		/*
		 * LoginRequireFieldException (AjaxAuthenticationFilter)
		 * 	- no memberId or no memberPassowrd
		 * 
		 * UsernameNotFoundException (UserDetailsServiceImpl)
		 * 	- not found memberId
		 * 
		 * BadCredentialsException (AjaxAuthenticationProvider)
		 * 	- not matched rawPassword, encPassword
		 */
		
		String message = MessageUtils.getMessage("error.Login");
		
		if (e instanceof LoginRequireFieldException || e instanceof UsernameNotFoundException
				|| e instanceof BadCredentialsException) {
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
			message = e.getMessage();
			//message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".<br>"), "<br>");
		}
		
		log.info("\t > redirectUrl = '{}'", getRedirectURL(request, response));
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(message)
				.build();
		log.info("\t > {}", errorResponse);
		
		log.info("\t > respond with errorResponse in JSON format");
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		response.getWriter().close();
	}
	
	private String getRedirectURL(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest == null) {
			return request.getSession().getServletContext().getContextPath();
		}
		return savedRequest.getRedirectUrl();
	}
}
