package com.codingjoa.websocket.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InboundChannelInterceptor implements ChannelInterceptor {
	
	private final ObjectMapper objectMapper;
	
	public InboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean preReceive(MessageChannel channel) {
		log.info("## {}.preReceive", this.getClass().getSimpleName());
		return true;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		log.info("## {}.preSend", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();
		log.info("\t > command = {}, destination = {}", command, accessor.getDestination());
		log.info("\t > {}", message);
		
		if (command == StompCommand.SEND) {
			Object payload = message.getPayload();
			if (payload instanceof byte[]) {
				byte[] bytes = (byte[]) payload;
				try {
					log.info("\t > payload = {}", objectMapper.readValue(bytes, Map.class));
				} catch (IOException e) {
					String decoded = new String(bytes, StandardCharsets.UTF_8);
					log.info("\t > payload = {}", decoded);
				}
			}
		}
		
		return message;
	}
	
}
