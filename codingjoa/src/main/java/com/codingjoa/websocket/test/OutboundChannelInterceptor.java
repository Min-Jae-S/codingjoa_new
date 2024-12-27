package com.codingjoa.websocket.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboundChannelInterceptor implements ChannelInterceptor {
	
	// outbound: preSend, postSend, afterSendCompletion
	
	private final ObjectMapper localMapper;
	
	public OutboundChannelInterceptor(ObjectMapper objectMapper) {
		// serialize the object excluding the "senderSessionId"
		this.localMapper = objectMapper.copy().addMixIn(ChatMessage.class, ChatMessageMixIn.class);
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
					
					byte[] modifiedPayload = localMapper.writeValueAsBytes(chatMessage);
					log.info("\t > modified payload: {}", FormatUtils.formatPrettyJson(modifiedPayload));
					
					// return the message with the modified payload
					return MessageBuilder.createMessage(modifiedPayload, message.getHeaders());
				} catch (JsonProcessingException e) {
					log.info("\t > payload: {}", new String(originalPayload, StandardCharsets.UTF_8));
				} catch (IOException e) {
					log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
				}
			}
		}
		
		return message;
	}
	
}
