package com.codingjoa.websocket.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboundChannelInterceptor implements ChannelInterceptor {
	
	private final ObjectMapper objectMapper;
	
	public OutboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		log.info("## {}", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();
		String ack = accessor.getAck();
		String nack = accessor.getNack();
		log.info("\t > command = {}, ack = {}, nack = {}", command, ack, nack);
		
		String destination = accessor.getDestination();
		log.info("\t > destination = {}", destination);
		//log.info("\t > {}", message);
		
		if (command == StompCommand.SEND) {
			Object payload = message.getPayload();
			if (payload instanceof byte[]) {
				byte[] bytes = (byte[]) payload;
				try {
					ChatMessage chatMessage = objectMapper.readValue(bytes, ChatMessage.class);
					log.info("\t > payload = {}", chatMessage);
					
					String senderSessionId = chatMessage.getSender();
					String receiverSessionId = accessor.getSessionId();
					
					if (receiverSessionId.equals(senderSessionId)) {
						log.info("\t > sender == reciever");
						return null;
					}
				} catch (IOException e) {
					String decoded = new String(bytes, StandardCharsets.UTF_8);
					log.info("\t > payload = {}", decoded);
				}
			}
		}
		
		return message;
	}
	
}
