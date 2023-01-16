package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

// "/board/main"
@Slf4j
public class CheckCriteriaInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== CheckCriteriaInterceptor ==============");
		log.info("{}", getFullURL(request));
		
		
		return true;
	}

	private boolean isNumeric(String param) {
		try {
			Integer.parseInt(param);
			return true;
		} catch (NumberFormatException e) {
			return false; 
		}
	}
	
	private String getFullURL(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	    	return requestURL.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }

	}

}
