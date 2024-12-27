package com.codingjoa.websocket.test;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboundChannelInterceptor implements ChannelInterceptor {
	
	// outbound: preSend, postSend, afterSendCompletion
	
	private final ObjectMapper localMapper;
	
	public OutboundChannelInterceptor(ObjectMapper objectMapper) {
		this.localMapper = objectMapper.copy();
	}
	
	@PostConstruct
	public void configureObjectMapper() {
		localMapper.addMixIn(ChatMessage.class, ChatMessageMixIn.class);
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		log.info("## {}", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		SimpMessageType messageType = accessor.getMessageType();
		StompCommand command = accessor.getCommand();
		log.info("\t > messageType: {}, command: {}", messageType, command);
		
		// outbound: CONNECT_ACK, DISCONNECT_ACK, MESSAGE, ERROR
		if (messageType == SimpMessageType.MESSAGE) {
			if (message.getPayload() instanceof byte[]) {
				byte[] originalPayload = (byte[]) message.getPayload();
				try {
					// deserialize the payload into ChatMessage
					ChatMessage chatMessage = localMapper.readValue(originalPayload, ChatMessage.class);
					
					// determine if sender and receiver sessionId match
					String senderSessionId = chatMessage.getSenderSessionId();
					String receiverSessionId = accessor.getSessionId();
					chatMessage.setSessionMatched(receiverSessionId.equals(senderSessionId));
					log.info("\t > final chatMessage = {}", chatMessage);
					
					// serialize the modified message excluding the "senderSessionId"
					byte[] modifiedPayload = localMapper.writeValueAsBytes(chatMessage);
					log.info("\t > modified payload: {}", FormatUtils.formatPrettyJson(modifiedPayload));
					
					// return the message with the modified payload
					return MessageBuilder.createMessage(modifiedPayload, message.getHeaders());
				} catch (Exception e) {
					log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
					String decodedPayload = new String(originalPayload, StandardCharsets.UTF_8);
					log.info("\t > payload: {}", decodedPayload);
				}
			}
		}
		
		return message;
	}
	
}
