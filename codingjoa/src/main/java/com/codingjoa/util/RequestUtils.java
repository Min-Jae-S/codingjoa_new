package com.codingjoa.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUtils {
	
	/*
	 * 	StringBuffer: thread-safe
	 * 	StringBuilder: single-thread
	 */

	public static String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		
		if (queryString == null) {
			return requestURL.toString();
		} else {
			//return requestURL.append("?").append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
			return requestURL.append("?").append(queryString).toString();
		}
	}
	
	public static String getFullURI(HttpServletRequest request) {
		StringBuffer requestURI = new StringBuffer(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8));
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	//return requestURI.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    	return requestURI.append("?").append(queryString).toString();
	    }
	}
	
	public static String getRequestLine(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		
		StringBuffer requestMethod = new StringBuffer(request.getMethod());
		String fullUri = getFullURI(request);
		return requestMethod.append(" '").append(fullUri).append("'").toString();
	}
	
	public static boolean isJsonAccept(HttpServletRequest request) {
		String accept = request.getHeader(HttpHeaders.ACCEPT);
		log.info("\t > accept: {}", accept);
		return (accept != null) && accept.contains(MediaType.APPLICATION_JSON_VALUE);
	}
	
	public static boolean isJsonContentType(HttpServletRequest request) {
		String contentType = request.getContentType();
		log.info("\t > contentType: {}", contentType);
		return (contentType != null) && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE);
	}
	
	public static boolean isJsonRequest(HttpServletRequest request) {
		return isJsonAccept(request) || isJsonContentType(request);
	}
	
}
