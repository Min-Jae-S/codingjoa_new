package com.codingjoa.interceptor.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestRestApiInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		log.info("=====================    REQUEST    =====================");
		log.info("## {}  {}  {}", request.getMethod(), request.getRequestURI(), request.getProtocol());
		log.info("\t > Host: {}", request.getHeader("host"));
		log.info("\t > Referer: {}", request.getHeader("referer"));
		log.info("\t > User-Agent: {}", request.getHeader("user-agent"));
		log.info("\t > Accept: {}", request.getHeader("accept"));
//		log.info("\t > Accept-Encoding: {}", request.getHeader("accept-encoding"));
//		log.info("\t > Accept-Language: {}", request.getHeader("accept-language"));
//		log.info("\t > Connection: {}", request.getHeader("connection"));
		log.info("=========================================================");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("=====================    RESPONSE   =====================");
		log.info("## {}  {}", request.getProtocol(), response.getStatus());
		log.info("\t > Content-Type: {}", response.getHeader("content-type"));
//		log.info("\t > Content-Length: {}", response.getHeader("content-length"));
		log.info("=========================================================");
	}
	
}
