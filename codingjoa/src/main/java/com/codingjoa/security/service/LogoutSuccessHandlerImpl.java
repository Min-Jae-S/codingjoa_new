package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	// private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	// Request URL: http://localhost:8888/codingjoa/logout
	// Request Method: GET
	// Status Code: 302 Found
	
	// redirect method (GET, POST)
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > authentication token = {}", (authentication != null) ? authentication.getClass().getSimpleName() : authentication);
		
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.timeZone(TimeZone.getTimeZone("Asia/Seoul"))
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
				.build();
		
		String redirectUrl = request.getContextPath() + "/login";
		log.info("\t > redirectUrl = '{}'", redirectUrl);
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Logout")
				.data(Map.of("redirectUrl", redirectUrl))
				.build();
		log.info("\t > {}", successResponse);
		
		log.info("\t > respond with successResponse in JSON format");
		response.getWriter().write(objectMapper.writeValueAsString(successResponse));
		response.getWriter().close();
		
		//response.sendRedirect(redirectUrl);
	}

}
