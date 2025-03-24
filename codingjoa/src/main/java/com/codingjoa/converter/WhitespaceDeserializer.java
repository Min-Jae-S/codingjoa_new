package com.codingjoa.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhitespaceDeserializer extends JsonDeserializer<String> { // used in CommentDto as a deserializer

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		log.info("## {}", this.getClass().getSimpleName());
			
		String input = p.getText();
		if (input != null) {
			log.info("\t > raw input = '{}'", input.replace("\n", "\\n"));
			log.info("\t > deserialized input = '{}'", input.strip().replace("\n", "\\n"));
			return input.trim();
		} else {
			log.info("\t > raw input = {}", input);
			log.info("\t > deserialized input = {}", input);
			return null;
		}
	}

}
