package com.codingjoa.security.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

/*	
 * 	@@ 인증 예외, 인증이 되지 않았을 경우(비로그인)
 * 
 * 	AuthenticationEntryPoint, commences an authentication scheme.
 * 	Implementations should modify the headers on the <code>ServletResponse</code> 
 * 	as necessary to commence the authentication process.
 */

@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > URI = {}", request.getRequestURI());
		log.info("\t > {} : {}", authException.getClass().getSimpleName(), authException.getMessage());
		
		/*
		 * https://0taeng.tistory.com/30
		 * https://mohwaproject.tistory.com/entry/Ajax-%EC%A0%84%EC%86%A1-%EA%B5%AC%EB%B6%84%ED%95%98%EA%B8%B0
		 * 
		 * 1. header: x-requested-with(key) --> XMLHttpRequest(value) 
		 * 		- x: Non-standard
		 * 		- jQuery나 대중성 있는 라이브러리들이 ajax 전송시 기본으로 추가하여 전송
		 * 
		 * 2. custom header
		 * 		beforeSend: function(xmlHttpRequest) {
		 * 			xmlHttpRequest.setRequestHeader("AJAX", "true")
		 * 		}
		 *
		 */
		
		if (isAjaxRequest(request)) {
			ErrorResponse errorResponse = ErrorResponse.builder()
					.status(HttpStatus.UNAUTHORIZED)
					.messageByCode("error.Unauthorized")
					.build();
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			
			ObjectMapper objectMapper = getObjectMapperWithSerializer();
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);
			
			log.info("\t > respond with error reponse in JSON format");
			response.getWriter().write(jsonResponse);
			response.getWriter().close();
		} else {
			String currentUrl = getFullURL(request);
			String redirectUrl = ServletUriComponentsBuilder.fromContextPath(request)
					.path("/login")
					.queryParam("redirect", URLEncoder.encode(currentUrl, StandardCharsets.UTF_8))
					.build()
					.toString();
			
			// http://localhost:8888/codingjoa/login?redirect=http%3A%2F%2Flocalhost%3A8888%2Fcodingjoa%2Fboard%2Fmodify%3FboardIdx%3D2082
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
	
	private String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL= request.getRequestURL();
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	    	return requestURL.append('?').append(queryString).toString();
	    }
	}
	
}
