package com.codingjoa.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntDeserializer extends JsonDeserializer<Integer> {

	@Override
	public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String input = p.getText();
		log.info("\t > input = {}", (input != null) ? "'" + input + "'" : null);
		
		return Integer.parseInt(input);
	}

}
