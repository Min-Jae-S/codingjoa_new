package com.codingjoa.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    
    public static String formatJson(String json) {
    	try {
    		JsonNode jsonNode = mapper.readTree(json);
			return System.lineSeparator() + writer.writeValueAsString(jsonNode);
		} catch (Exception e) {
			throw new RuntimeException("format json failure", e);
		}
    }
}
