package com.codingjoa.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
    
    // initialize objectMapper to enable pretty-printing
    static {
    	mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    public static String formatJson(Object obj) {
    	try {
    		if (obj instanceof String) {
        		JsonNode jsonNode = mapper.readTree((String) obj);
        		return System.lineSeparator() + mapper.writeValueAsString(jsonNode);
        	} else {
        		return System.lineSeparator() + mapper.writeValueAsString(obj);
        	}
    	} catch(Exception e) {
    		throw new RuntimeException("format json failure", e);
    	}
    }
}
