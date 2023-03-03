package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

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

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("============== CustomAuthenticationEntryPoint ==============");
		log.info("[default] response.getContentType()={}", response.getContentType());
		
		// 401(Unauthorized) vs 403(Forbidden), https://mangkyu.tistory.com/146
		//response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
	}

}
