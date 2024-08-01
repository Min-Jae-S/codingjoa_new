package com.codingjoa.util;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	public static boolean isPageNumber(String str) {
		try {
			int pageNumber = Integer.parseInt(str);
			return pageNumber > 0;
		} catch (Exception e) {
			return false;
		}
		
//		char tmp = str.charAt(0);
//		if (tmp <= '0' || tmp > '9') {
//			return false;
//		}
//		
//		for (int i = 1; i < str.length(); i++) {
//			tmp = str.charAt(i);
//			if(tmp < '0' || tmp > '9') {
//				return false;
//			}
//		}
	}
	
	public static String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		
		if (queryString == null) {
			return requestURL.toString();
		} else {
			//return requestURL.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
			return requestURL.append('?').append(queryString).toString();
		}
	}
	
	public static String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = 
				new StringBuilder(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8));
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	//return requestURI.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    	return requestURI.append('?').append(queryString).toString();
	    }
	}
	
	public static List<String> getFieldNames(Object object) {
		if (object == null) {
			return null;
		}
		
		Field[] fields = object.getClass().getDeclaredFields();
		return Arrays.stream(fields)
				.map(field -> field.getName())
				.collect(Collectors.toList());
	}
	
}
