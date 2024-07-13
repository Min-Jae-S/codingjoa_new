package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
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
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(message)
				.build();
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		ObjectMapper objectMapper = getObjectMapperWithSerializer();
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		
		log.info("\t > respond with error response in JSON format");
		response.getWriter().write(jsonResponse);
		response.getWriter().close();
	}
	
	private ObjectMapper getObjectMapperWithSerializer() {
        return Jackson2ObjectMapperBuilder
                .json()
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
	
}
