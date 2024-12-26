package com.codingjoa.util;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormatUtils {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	// initialize objectMapper
    static {
    	DateTimeFormatter localDateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm"); 
		DateTimeFormatter localTimeformatter = DateTimeFormatter.ofPattern("HH:ss"); 
    	objectMapper.registerModule(new SimpleModule()
    		.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(localDateTimeformatter))
    		.addSerializer(LocalTime.class, new LocalTimeSerializer(localTimeformatter))
    	);
    	
    	// enable pretty-printing
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
	
	public static List<String> formatFields(Object object) {
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
	
	public static String formatPrettyJson(Object obj) {
    	if (obj == null) {
    		return null;
    	}
    	
    	try {
    		String json;
    		if (obj instanceof String) {
    			json = (String) obj;
    		} else if (obj instanceof byte[]) {
    			json = new String((byte[]) obj, StandardCharsets.UTF_8);
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
