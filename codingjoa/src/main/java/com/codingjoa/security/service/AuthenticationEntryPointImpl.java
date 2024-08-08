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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.Utils;
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

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", Utils.getHttpRequestLine(request));
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
			
			log.info("\t > respond with error response in JSON format");
			response.getWriter().write(jsonResponse);
			response.getWriter().close();
		} else {
			String loginUrl = buildLoginUrl(request);
			redirectStrategy.sendRedirect(request, response, loginUrl);
		}
	}
	
	private String buildLoginUrl(HttpServletRequest request) {
		String currentUrl = UrlUtils.buildFullRequestUrl(request);
		return ServletUriComponentsBuilder.fromContextPath(request)
				.path("/login")
				.queryParam("continue", UriUtils.encode(currentUrl, StandardCharsets.UTF_8))
				.build()
				.toUriString();
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
	
}
