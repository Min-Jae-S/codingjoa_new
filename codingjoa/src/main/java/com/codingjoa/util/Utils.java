package com.codingjoa.util;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	// initialize objectMapper to enable pretty-printing
    static {
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

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
	
	/*
	 * StringBuffer, thread-safe
	 * StringBuilder, single-thread
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
	
	public static String getHttpRequestLine(HttpServletRequest request) {
		StringBuffer requestMethod = new StringBuffer(request.getMethod());
		String fullUri = getFullURI(request);
		return requestMethod.append(" '").append(fullUri).append("'").toString();
	}
	
	public static List<String> specifiyFields(Object object) {
		if (object == null) {
			return null;
		}
		
		Class<?> clazz = object.getClass();
		List<Field> fields = new ArrayList<>();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				fields.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		
		return fields.stream()
				.map(field -> field.getName())
				.collect(Collectors.toList());
	}
	
	public static String formatJson(Object obj) {
    	if (obj == null) {
    		return null;
    	}
    	
    	try {
    		String json;
    		if (obj instanceof String) {
    			json = (String) obj;
    		} else {
    			json = objectMapper.writeValueAsString(obj);
    		}
    		
    		JsonNode jsonNode = objectMapper.readTree(json);
    		return System.lineSeparator() + objectMapper.writeValueAsString(jsonNode);
    	} catch(Exception e) {
    		log.error("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
    		return null;
    	}
    }
	
	public static String formatString(String input) {
		return (input == null) ? null : "'" + input + "'";
	}
		
	
}
