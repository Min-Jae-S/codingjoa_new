package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.AjaxUtils;
import com.codingjoa.util.HttpUtils;
import com.codingjoa.util.UriUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*	
 * 	@@ 인증 예외, 인증이 되지 않았을 경우(비로그인)
 * 
 * 	AuthenticationEntryPoint, commences an authentication scheme.
 * 	Implementations should modify the headers on the <code>ServletResponse</code> 
 * 	as necessary to commence the authentication process.
 */

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

	private static final String FORWARD_PATH = "/WEB-INF/views/router/alert-and-redirect.jsp";
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private final ObjectMapper objectMapper;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getRequestLine(request));
		log.info("\t > {}: {}", authException.getClass().getSimpleName(), authException.getMessage());
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
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
		 */
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.messageByCode("error.Unauthorized")
				.build();
		
		if (AjaxUtils.isAjaxRequest(request)) {
			log.info("\t > respond with errorResponse in JSON format");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(jsonResponse);
			response.getWriter().close();
		} else {
//			request.setAttribute("continueUrl", UriUtils.buildLoginUrl(request));
//			request.setAttribute("message", errorResponse.getMessage());
//
//			log.info("\t > forward to 'alert-and-redirect.jsp'");
//			request.getRequestDispatcher(FORWARD_PATH).forward(request, response);

			redirectStrategy.sendRedirect(request, response, UriUtils.buildLoginUrl(request));
		}
	}
}
