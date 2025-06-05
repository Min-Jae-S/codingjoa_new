package com.codingjoa.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RequestUtils {
	
	/*
	 * 	StringBuffer: thread-safe
	 * 	StringBuilder: single-thread
	 */
	
	private RequestUtils() {}

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
		StringBuffer requestURI = new StringBuffer(request.getRequestURI());
		//StringBuffer requestURI = new StringBuffer(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8));
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
		return (accept != null) && accept.contains(MediaType.APPLICATION_JSON_VALUE);
	}
	
	public static boolean isJsonContentType(HttpServletRequest request) {
		String contentType = request.getContentType();
		return (contentType != null) && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE);
	}

	public static boolean isApiPath(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return (uri != null) && uri.startsWith(request.getContextPath() + "/api/");
	}
	
	public static boolean isRestApiRequest(HttpServletRequest request) {
		return isApiPath(request) || isJsonAccept(request);
	}
	
}
