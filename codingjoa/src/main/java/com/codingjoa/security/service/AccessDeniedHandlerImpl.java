package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 	@@ 인가 예외, 인증(로그인)은 되었으나 해당 요청에 대한 권한이 없을 경우
 * 
 * 	AccessDeniedHandler, handles an access denied failure.
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	
	private final ObjectMapper objectMapper;
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
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
			
			log.info("\t > respond with error response in JSON format");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);
			response.getWriter().write(jsonResponse);
			response.getWriter().close();
		} else {
			redirectStrategy.sendRedirect(request, response, request.getContextPath() + "/");
		}
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
	}
	
}
