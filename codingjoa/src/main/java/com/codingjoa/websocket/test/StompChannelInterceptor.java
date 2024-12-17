package com.codingjoa.websocket.test;

import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StompChannelInterceptor implements ChannelInterceptor {
	
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
			String decoded = new String(bytes, StandardCharsets.UTF_8);
			log.info("\t > payload = {}", decoded);
		}
		
		return message;
	}
	
}
