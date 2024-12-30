package com.codingjoa.websocket.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
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
		this.localMapper = objectMapper.copy().addMixIn(StompMessage.class, StompMessageMixIn.class);
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		log.info("## {}", this.getClass().getSimpleName());
		//ExecutorSubscribableChannel channel = (ExecutorSubscribableChannel) messageChannel;
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		log.info("\t > messageType: {}, command: {}", accessor.getMessageType(), accessor.getCommand());
		
		// outbound: CONNECT_ACK, DISCONNECT_ACK, MESSAGE, ERROR
		if (accessor.getMessageType() == SimpMessageType.MESSAGE) {
			if (message.getPayload() instanceof byte[]) {
				byte[] originalPayload = (byte[]) message.getPayload();
				try {
					// deserialize the payload into ChatMessage
					StompMessage stompMessage = localMapper.readValue(originalPayload, StompMessage.class);
					
					// determine if sender and receiver sessionId match
					String senderSessionId = stompMessage.getSenderSessionId();
					String receiverSessionId = accessor.getSessionId();
					StompMessage modifiedMessage = stompMessage.toBuilder()
							.sessionMatched((receiverSessionId.equals(senderSessionId)))
							.build();
					
					byte[] modifiedPayload = localMapper.writeValueAsBytes(modifiedMessage);
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
