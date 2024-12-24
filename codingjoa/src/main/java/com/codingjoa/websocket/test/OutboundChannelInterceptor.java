package com.codingjoa.websocket.test;

import java.nio.charset.StandardCharsets;

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
public class OutboundChannelInterceptor implements ChannelInterceptor {
	
	private final ObjectMapper objectMapper;
	
	public OutboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		log.info("## {}", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		SimpMessageType messageType = accessor.getMessageType();
		StompCommand command = accessor.getCommand();
		MessageHeaders headers = accessor.getMessageHeaders();
		
		// outbound: CONNECT_ACK, DISCONNECT_ACK, MESSAGE, ERROR
		if (messageType == SimpMessageType.MESSAGE) {
			log.info("\t > messageType: {}, command: {}", messageType, command);
			Object payload = message.getPayload();
			if (payload instanceof byte[]) {
				byte[] bytes = (byte[]) payload;
				try {
					ChatMessage chatMessage = objectMapper.readValue(bytes, ChatMessage.class);
					log.info("\t > payload: {}", FormatUtils.formatPrettyJson(chatMessage));
					
					String senderSessionId = chatMessage.getSender();
					String recieverSessionId = accessor.getSessionId();
					log.info("\t > sender sessionId: {}", senderSessionId);
					log.info("\t > reciever sessionId: {}", recieverSessionId);
					
					if (recieverSessionId.equals(senderSessionId)) {
						log.info("\t > sender and reciever are identical");
					}
				} catch (Exception e) {
					String decoded = new String(bytes, StandardCharsets.UTF_8);
					log.info("\t > payload: {}", decoded);
				}
			}
		} else {
			log.info("\t > messageType: {}, command: {} {}", messageType, command, FormatUtils.formatPrettyJson(headers));
		}
		
		return message;
	}

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
