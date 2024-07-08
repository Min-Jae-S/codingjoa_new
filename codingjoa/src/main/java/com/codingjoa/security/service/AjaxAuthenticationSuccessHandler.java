package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private final JwtProvider jwtProvider;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String token = jwtProvider.createToken(request, authentication);
		String redirectUrl = resolveRedirectUrl(request, authentication);
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Login")
				.data(Map.of("token", token, "redirectUrl", redirectUrl))
				.build();
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		ObjectMapper objectMapper = getObjectMapperWithSerializer();
		String jsonResponse = objectMapper.writeValueAsString(successResponse);
        
		log.info("\t > respond with success response in JSON format");
		response.getWriter().write(jsonResponse);
		response.getWriter().close();
	}
	
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		log.info("## {}", this.getClass().getSimpleName());
//		
//		String redirectUrl = (String) authentication.getDetails();
//		
//		if (!isValidUrl(request, redirectUrl)) {
//			log.info("\t > invalid redirectUrl - default redirectUrl will be set");
//			redirectUrl = ServletUriComponentsBuilder.fromContextPath(request)
//					.path("/")
//					.build()
//					.toString();
//		} else {
//			log.info("\t > valid redirectUrl");
//		}
//		
//		clearAuthenticationDetails(authentication);
//		
//		SuccessResponse successResponse = SuccessResponse.builder()
//				.status(HttpStatus.OK)
//				.messageByCode("success.Login")
//				.data(Map.of("redirectUrl", redirectUrl))
//				.build();
//		
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//		
//		ObjectMapper objectMapper = getObjectMapperWithSerializer();
//		String jsonResponse = objectMapper.writeValueAsString(successResponse);
//		
//		response.getWriter().write(jsonResponse);
//		response.getWriter().close();
//	}
	
	private String resolveRedirectUrl(HttpServletRequest request, Authentication authentication) {
		String redirectUrl = (String) authentication.getDetails();
		
		if (!isValidUrl(request, redirectUrl)) {
			log.info("\t > missing or invalid redirectUrl, setting default redirectUrl");
			return ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toString();
		} else {
			log.info("\t > valid redirectUrl, setting redirectUrl from request");
			return redirectUrl;
		}
	}
	
	private boolean isValidUrl(HttpServletRequest request, String url) {
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		StringBuffer requestURL = request.getRequestURL(); 				// http://localhost:8888/codingjoa/**
		String contextPath = request.getContextPath();					// /codingjoa
		
		int contextPathIndex = requestURL.indexOf(contextPath) + contextPath.length();
		String baserUrl = requestURL.substring(0, contextPathIndex);	// http://localhost:8888/codingjoa
		
		return new AntPathMatcher().match(baserUrl + "/**", url);
	}
	
	@SuppressWarnings("unused")
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