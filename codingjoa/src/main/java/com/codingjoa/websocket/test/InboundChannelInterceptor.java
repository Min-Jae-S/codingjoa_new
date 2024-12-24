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

@SuppressWarnings("rawtypes")
@Slf4j
public class InboundChannelInterceptor implements ChannelInterceptor {

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
		MessageHeaders headers = accessor.getMessageHeaders();
		log.info("\t > messageType: {}, command: {} {}", messageType, command, FormatUtils.formatPrettyJson(headers));
		
		// inbound: CONNECT, SUBSCRIBE, SEND, DISCONNECT
		if (command == StompCommand.SEND) {
			Object payload = message.getPayload();
			if (payload instanceof byte[]) {
				byte[] bytes = (byte[]) payload;
				try {
					Map map = objectMapper.readValue(bytes, Map.class);
					log.info("\t > payload: {}", FormatUtils.formatPrettyJson(map));
				} catch (IOException e) {
					String decoded = new String(bytes, StandardCharsets.UTF_8);
					log.info("\t > payload: {}", decoded);
				}
			}
		}
		
		return message;
	}
	
//	@Override
//	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
//		//log.info("## {}", this.getClass().getSimpleName());
//		//ExecutorSubscribableChannel channel = (ExecutorSubscribableChannel) messageChannel;
//		//log.info("\t > subscribers = {}", channel.getSubscribers());
//		
//		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//		MessageHeaders headers = accessor.getMessageHeaders();
//		String headerJson = FormatUtils.formatPrettyJson(headers);
//		if (headerJson != null) {
//			log.info("## {} {}", this.getClass().getSimpleName(), headerJson);
//		} else {
//			log.info("## {}", this.getClass().getSimpleName());
//			headers.keySet().forEach(key -> log.info("\t > {}: {}", key, headers.get(key)));
//		}
//		
//		// inbound: CONNECT, SUBSCRIBE, SEND, DISCONNECT
//		if (accessor.getCommand() == StompCommand.SEND) {
//			Object payload = message.getPayload();
//			if (payload instanceof byte[]) {
//				byte[] bytes = (byte[]) payload;
//				try {
//					log.info("\t > payload = {}", objectMapper.readValue(bytes, Map.class));
//				} catch (IOException e) {
//					String decoded = new String(bytes, StandardCharsets.UTF_8);
//					log.info("\t > payload = {}", decoded);
//				}
//			}
//		}
//		
//		return message;
//	}
	
	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		if (!sent || ex != null) {
			log.info("## {}.afterSendCompletion", this.getClass().getSimpleName());
			if (ex != null) {
				log.info("\t > {} : {}", ex.getClass().getSimpleName(), ex.getMessage());
			}
			
			if (!sent) {
				log.info("\t > not sent");
			}
		}
	}
	
}
