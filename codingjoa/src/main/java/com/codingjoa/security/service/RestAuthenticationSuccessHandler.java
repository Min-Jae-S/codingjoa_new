package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.service.UrlValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired(required = false)
	private UrlValidationService urlValidationService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > authentication = {} ({})", authentication.getClass().getSimpleName(),
				authentication.isAuthenticated() == true ? "authenticated" : "unauthenticated");
		
		String redirectUrl = obtainRedirectUrl(authentication);
		if (!urlValidationService.validateUrl(request, redirectUrl)) {
			redirectUrl = request.getContextPath() + "/";
		}
		
		clearAuthenticationDetails(authentication);
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Login")
				.data(Map.of("redirectUrl", redirectUrl))
				.build();
		log.info("\t > {}", successResponse);
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		ObjectMapper objectMapper = getObjectMapperWithSerializer();
		String jsonResponse = objectMapper.writeValueAsString(successResponse);
        
		response.getWriter().write(jsonResponse);
		response.getWriter().close();
	}
	
	private String obtainRedirectUrl(Authentication authentication) {
		Object details = authentication.getDetails();
        return (details instanceof String) ? (String) details : null;
	}
	
	private void clearAuthenticationDetails(Authentication authentication) {
		log.info("\t > clear authentication details");
		((UsernamePasswordAuthenticationToken) authentication).setDetails(null);
	}
	
	private ObjectMapper getObjectMapperWithSerializer() {
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
        return Jackson2ObjectMapperBuilder
                .json()
              //.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

}
