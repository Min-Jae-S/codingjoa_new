package com.codingjoa.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // initialize objectMapper to enable pretty-printing
    static {
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
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
    
}
