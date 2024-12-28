package com.codingjoa.websocket.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InboundChannelInterceptor implements ChannelInterceptor { 

	// inbound: preReceive, preSend, postSend, afterSendCompletion

	private final ObjectMapper objectMapper;
	
	public InboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		log.info("## {}", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		SimpMessageType messageType = accessor.getMessageType();
		StompCommand command = accessor.getCommand();
		log.info("\t > messageType: {}, command: {}", messageType, command);
		
		// inbound: CONNECT, SUBSCRIBE, SEND, DISCONNECT
		if (command == StompCommand.SEND) {
			//log.info("\t > headers: {}", FormatUtils.formatPrettyJson(message.getHeaders()));
			if (message.getPayload() instanceof byte[]) {
				byte[] payload = (byte[]) message.getPayload();
				try {
					StompMessage stompMessage = objectMapper.readValue(payload, StompMessage.class);
					log.info("\t > payload: {}", FormatUtils.formatPrettyJson(stompMessage));
				} catch (JsonProcessingException e) {
					log.info("\t > payload: {}", new String(payload, StandardCharsets.UTF_8));
				} catch (IOException e) {
					log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
				}
			}
		}
		
		return message;
	}
	
}
