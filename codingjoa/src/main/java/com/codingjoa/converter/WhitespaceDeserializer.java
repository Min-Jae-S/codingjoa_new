package com.codingjoa.converter;

import java.io.IOException;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhitespaceDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) 
			throws IOException, JsonProcessingException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String input = p.getText();
		log.info("\t > input = {}", input != null ? "'" + StringEscapeUtils.escapeJava(input) + "'" : null);
		
		return input != null ? input.strip() : null;
	}

}
