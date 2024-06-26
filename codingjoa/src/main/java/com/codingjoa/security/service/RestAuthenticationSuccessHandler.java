package com.codingjoa.security.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.codingjoa.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > authentication = {} ({})", authentication.getClass().getSimpleName(),
				authentication.isAuthenticated() == true ? "authenticated" : "unauthenticated");
		
		String redirectUrl = retrieveRedirectUrl(request, authentication);
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
	
	private String retrieveRedirectUrl(HttpServletRequest request, Authentication authentication) {
		Object details = authentication.getDetails();
        String redirectUrl = (details instanceof String) ? (String) details : null;
        log.info("\t > initial redirectUrl = {}", redirectUrl);
		
        if (!isValidUrl(redirectUrl)) {
        	redirectUrl = request.getContextPath() + "/";
		}
        log.info("\t > final redirectUrl = {}", redirectUrl);
        
		return redirectUrl;
	}
	
	
	private boolean isValidUrl(String url) {
		// URL format validation: verify that the URL is in the correct format
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		try {
			URL parsedUrl = new URL(url);
			// Allowed domain validation: check if the redirection URL is in the list of allowed domains
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
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
