package com.codingjoa.websocket.test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StompChannelInterceptor implements ChannelInterceptor {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public boolean preReceive(MessageChannel channel) {
		log.info("## {}.preReceive", this.getClass().getSimpleName());
		return true;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		log.info("## {}.preSend", this.getClass().getSimpleName());
		log.info("\t > {}", message);
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		log.info("\t > command = {}", accessor.getCommand());
		log.info("\t > destination = {}", accessor.getDestination());
		
		Object payload = message.getPayload();
		if (payload instanceof byte[]) {
			byte[] bytes = (byte[]) payload;
			try {
				log.info("\t > payload = {}", objectMapper.readValue(bytes, Map.class));
			} catch (JsonParseException e) {
				String decoded = new String(bytes, StandardCharsets.UTF_8);
				log.info("\t > payload = {}", decoded);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return message;
	}
	
}
