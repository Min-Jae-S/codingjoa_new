package com.codingjoa.websocket.test;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class OutboundChannelInterceptor implements ChannelInterceptor {
	
	private final ObjectMapper objectMapper;
	
	public OutboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		//log.info("## {}", this.getClass().getSimpleName());
		//ExecutorSubscribableChannel channel = (ExecutorSubscribableChannel) messageChannel;
		//log.info("\t > subscribers = {}", channel.getSubscribers());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		MessageHeaders headers = accessor.getMessageHeaders();
		String headerJson = FormatUtils.formatPrettyJson(headers);
		if (headerJson != null) {
			log.info("## {} {}", this.getClass().getSimpleName(), headerJson);
		} else {
			log.info("## {}", this.getClass().getSimpleName());
			headers.keySet().forEach(key -> log.info("\t > {}: {}", key, headers.get(key)));
		}
		
		// outbound: CONNECT_ACK, DISCONNECT_ACK, MESSAGE, ERROR
//		SimpMessageType messageType = accessor.getMessageType();
//		log.info("\t > simpMessageType: {}", messageType);
//
//		StompCommand command = accessor.getCommand();
//		if (command != null) {
//			log.info("\t > STOMP command: {}", command);
//			
//			if (command == StompCommand.SEND) {
//				Object payload = message.getPayload();
//				if (payload instanceof byte[]) {
//					byte[] bytes = (byte[]) payload;
//					try {
//						ChatMessage chatMessage = objectMapper.readValue(bytes, ChatMessage.class);
//						log.info("\t > payload = {}", chatMessage);
//						
//						String senderSessionId = chatMessage.getSender();
//						String receiverSessionId = accessor.getSessionId();
//						if (receiverSessionId.equals(senderSessionId)) {
//							log.info("\t > sender and reciever are identical");
//							return null;
//						}
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
		if (!sent || ex != null) {
			log.info("## {}.afterSendCompletion", this.getClass().getSimpleName());
			if (ex != null) {
				log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
			}
			
			if (!sent) {
				log.info("\t > not sent");
			}
		}
	}
	
}
