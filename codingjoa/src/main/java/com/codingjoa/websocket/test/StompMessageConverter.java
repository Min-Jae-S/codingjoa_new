package com.codingjoa.websocket.test;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StompMessageConverter extends MappingJackson2MessageConverter {

	@Override
	protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
		log.info("## {}.convertToInternal", this.getClass().getSimpleName());
		log.info("\t > payload = {}", payload);
		log.info("\t > headers = {}", headers);
		log.info("\t > conversionHint = {}", conversionHint);

		return super.convertToInternal(payload, headers, conversionHint);
	}
	
}
