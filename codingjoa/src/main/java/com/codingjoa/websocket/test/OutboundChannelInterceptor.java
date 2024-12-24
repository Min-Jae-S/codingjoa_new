package com.codingjoa.websocket.test;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class OutboundChannelInterceptor implements ChannelInterceptor {
	
	// outbound: preSend, postSend, afterSendCompletion
	
	private final ObjectMapper objectMapper;
	
	public OutboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		log.info("## {}.preSend", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		SimpMessageType messageType = accessor.getMessageType();
		StompCommand command = accessor.getCommand();
		log.info("\t > messageType: {}, command: {}", messageType, command);
		
		Object payload = message.getPayload();
		if (payload instanceof ChatMessage) {
			log.info("\t > payload is instanceof ChatMessage");
			ChatMessage chatMessage = (ChatMessage) payload;
			log.info("\t > chatMessage = {}", chatMessage);
		} else {
			log.info("\t > payload is not instanceof ChatMessage");
		}
		
		/*
		// outbound: CONNECT_ACK, DISCONNECT_ACK, MESSAGE, ERROR
		if (messageType == SimpMessageType.MESSAGE) {
			Object payload = message.getPayload();
			if (payload instanceof byte[]) {
				try {
					ChatMessage chatMessage = objectMapper.readValue((byte[]) payload, ChatMessage.class);
					String senderSessionId = chatMessage.getSender();
					String receiverSessionId = accessor.getSessionId();
					log.info("\t > sender sessionId: {}", senderSessionId);
					log.info("\t > receiver sessionId: {}", receiverSessionId);
					
					if (receiverSessionId.equals(senderSessionId)) {
						log.info("\t > sender and receiver are the same session");
						chatMessage.setSessionMatched(true);
					} else {
						log.info("\t > sender and receiver are the different session");
						chatMessage.setSessionMatched(false);
					}
					
					log.info("\t > payload: {}", FormatUtils.formatPrettyJson(chatMessage));
				} catch (Exception e) {
					String decoded = new String(bytes, StandardCharsets.UTF_8);
					log.info("\t > payload: {}", decoded);
				}
			}
		}
		*/
		
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
