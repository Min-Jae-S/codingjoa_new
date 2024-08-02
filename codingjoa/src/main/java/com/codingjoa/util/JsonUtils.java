package com.codingjoa.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
    
    // initialize objectMapper to enable pretty-printing
    static {
    	mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    public static String formatJson(Object obj) {
    	try {
    		String json = mapper.writeValueAsString(obj);
    		JsonNode jsonNode = mapper.readTree((String) json);
    		return System.lineSeparator() + mapper.writeValueAsString(jsonNode);
    	} catch(Exception e) {
    		log.error("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
    		return null;
    	}
    }
    
}
