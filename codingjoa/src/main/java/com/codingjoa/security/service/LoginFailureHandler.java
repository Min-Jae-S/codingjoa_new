package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private final String key = UUID.randomUUID().toString();
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > referer = {}", request.getHeader("referer"));
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("\t > current authentication token = {}", 
				(authentication != null) ? authentication.getClass().getSimpleName() : authentication);
		
		if (authentication == null) {
			SecurityContextHolder.getContext().setAuthentication(createAuthentication(request));
		}
		
		String message = MessageUtils.getMessage("error.Login");
		if (e instanceof LoginRequireFieldException || 
				e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
			log.info("\t > {}", e.getClass().getSimpleName());
			message = e.getMessage();
		}
		log.info("\t > original message = {}", message);
		
		message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".<br>"), "<br>");
		log.info("\t > processed message = {}", message);
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(message)
				.build();
		log.info("\t > {}", errorResponse);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.timeZone(TimeZone.getTimeZone("Asia/Seoul"))
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
				.build();
		log.info("\t > respond with errorResponse in JSON format");
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		response.getWriter().flush();
		
	}
	
	// ref) AnonymousAuthenticationFilter#createAuthentication(HttpServletRequest)
	protected Authentication createAuthentication(HttpServletRequest request) {
		log.info("\t > create authentication token (AnonymousAuthenticationToken)");
		
		// null object pattern 
		AnonymousAuthenticationToken token = new AnonymousAuthenticationToken(key, "anonymousUser",
				AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")); 
		token.setDetails(authenticationDetailsSource.buildDetails(request));
		return token;
	}
}
