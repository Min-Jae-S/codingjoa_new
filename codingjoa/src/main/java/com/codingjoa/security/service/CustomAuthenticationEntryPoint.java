package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.codingjoa.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/*	
 * 	인증이 되지 않았을 경우(비로그인)
 * 
 * 	AuthenticationEntryPoint, Commences an authentication scheme.
 * 
 * 	Implementations should modify the headers on the <code>ServletResponse</code> as
 *  necessary to commence the authentication process.
 *  
 */

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final String DEFAULT_FAILURE_URL = "/member/login";
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("============== CustomAuthenticationEntryPoint ==============");
		
		/*	# ajax 요청 확인 
		 	https://0taeng.tistory.com/30
		 	https://mohwaproject.tistory.com/entry/Ajax-%EC%A0%84%EC%86%A1-%EA%B5%AC%EB%B6%84%ED%95%98%EA%B8%B0
		
		 	1. header: x-requested-with(key) --> XMLHttpRequest(value) 
				- x: Non-standard
				- jquery나 대중성 있는 라이브러리들이 ajax전송시 기본으로 추가하여 전송
		
		 	2. custom header
				beforeSend: function(xmlHttpRequest) {
					xmlHttpRequest.setRequestHeader("AJAX", "true")
				}
				...
		*/
		
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("x-requested-with")) ? true : false;
		log.info("ajax={}", ajax);
		
		if (ajax) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(response.getWriter(), ErrorResponse.create().errorCode("error.NotLogin"));
		} else {
			request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
		}
		
	}

}
