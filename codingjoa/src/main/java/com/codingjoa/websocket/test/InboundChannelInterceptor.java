package com.codingjoa.websocket.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class InboundChannelInterceptor implements ChannelInterceptor {

	private final ObjectMapper objectMapper;
	
	public InboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		//log.info("## {}", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		MessageHeaders headers = accessor.getMessageHeaders();
		log.info("## {} {}", this.getClass().getSimpleName(), FormatUtils.formatPrettyJson(headers));
		log.info("\t > channel = {}", channel.getClass().getSimpleName());
		
		// inbound: CONNECT, SUBSCRIBE, SEND, DISCONNECT
//		SimpMessageType messageType = accessor.getMessageType();
//		log.info("\t > simpMessageType: {}", messageType);

//		StompCommand command = accessor.getCommand();
//		if (command != null) {
//			log.info("\t > STOMP command: {}", command);
//			
//			if (command == StompCommand.SEND) {
//				Object payload = message.getPayload();
//				if (payload instanceof byte[]) {
//					byte[] bytes = (byte[]) payload;
//					try {
//						log.info("\t > payload = {}", objectMapper.readValue(bytes, Map.class));
//					} catch (IOException e) {
//						String decoded = new String(bytes, StandardCharsets.UTF_8);
//						log.info("\t > payload = {}", decoded);
//					}
//				}
//			}
//		}
		
		//String destination = accessor.getDestination();
		//log.info("\t > destination = {}", destination);
		//log.info("\t > {}", message);
		
		return message;
	}
	
	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		log.info("## {}.afterSendCompletion, sent = {}", this.getClass().getSimpleName(), sent);
	}
	
}
