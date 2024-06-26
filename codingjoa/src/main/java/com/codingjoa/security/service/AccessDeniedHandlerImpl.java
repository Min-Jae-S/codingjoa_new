package com.codingjoa.security.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

/*
 * 	@@ 인가 예외, 인증(로그인)은 되었으나 해당 요청에 대한 권한이 없을 경우
 * 
 * 	AccessDeniedHandler, handles an access denied failure.
 */

@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
		log.info("\t > {} : {}", accessDeniedException.getClass().getSimpleName(), accessDeniedException.getMessage());
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("\t > authentication = {}", (authentication != null) ? authentication.getClass().getSimpleName() : null);
		
		if (isAjaxRequest(request)) {
			ErrorResponse errorResponse = ErrorResponse.builder()
					.status(HttpStatus.FORBIDDEN)
					.messageByCode("error.Forbidden")
					.build();
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			
			ObjectMapper objectMapper = getObjectMapperWithSerializer();
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);
			
			response.getWriter().write(jsonResponse);
			response.getWriter().close();
		} else {
			String redirectUrl = request.getContextPath() + "/";
			log.info("\t > redirect to '{}'", redirectUrl);
			
			response.sendRedirect(redirectUrl);
		}
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
	}
	
	private ObjectMapper getObjectMapperWithSerializer() {
        return Jackson2ObjectMapperBuilder
                .json()
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) // yyyy-MM-dd'T'HH:ss:mm"
                .build();
    }
	
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = 
				new StringBuilder(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8));
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?')
	    			.append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
	
}
