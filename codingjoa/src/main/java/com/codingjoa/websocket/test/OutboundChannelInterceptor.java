package com.codingjoa.websocket.test;

import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class OutboundChannelInterceptor implements ChannelInterceptor {
	
	// outbound: preSend, postSend, afterSendCompletion
	
	private final ObjectMapper objectMapper;
	
	public OutboundChannelInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		log.info("## {}", this.getClass().getSimpleName());
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		SimpMessageType messageType = accessor.getMessageType();
		StompCommand command = accessor.getCommand();
		log.info("\t > messageType: {}, command: {}", messageType, command);
		
		// outbound: CONNECT_ACK, DISCONNECT_ACK, MESSAGE, ERROR
		if (messageType == SimpMessageType.MESSAGE) {
			Object payload = message.getPayload();
			if (payload instanceof byte[]) {
				try {
					// deserialize the payload into ChatMessage
					ChatMessage chatMessage = objectMapper.readValue((byte[]) payload, ChatMessage.class);
					
					// determine if sender and receiver sessionId match
					String senderSessionId = chatMessage.getSenderSessionId();
					String receiverSessionId = accessor.getSessionId();
					chatMessage.setSessionMatched(receiverSessionId.equals(senderSessionId));
					
					// serialize the modified message excluding the "senderSessionId"
					SimpleFilterProvider filterProvider = new SimpleFilterProvider()
						    .addFilter("ChatMessageFilter", SimpleBeanPropertyFilter.filterOutAllExcept("senderSessionId"));
					ObjectWriter writer = objectMapper.writer(filterProvider);
					
					String json = writer.writeValueAsString(chatMessage);
					log.info("\t > serialized json: {}", FormatUtils.formatPrettyJson(json));
					
					byte[] modifiedPayload = json.getBytes(StandardCharsets.UTF_8);
					log.info("\t > modified payload: {}", new String(modifiedPayload, StandardCharsets.UTF_8));
					
					//log.info("\t > headers");
					//accessor.getMessageHeaders().forEach((key, value) -> log.info("\t\t - {}: {}", key, value));
					return MessageBuilder.fromMessage(message).build();
					
					// return the modified message with the modified payload
					//return MessageBuilder.createMessage(modifiedPayload, accessor.getMessageHeaders());
					//return message;
				} catch (Exception e) {
					String decodedPayload = new String((byte[]) payload, StandardCharsets.UTF_8);
					log.info("\t > payload: {}", decodedPayload);
				}
			}
		}
		
		return message;
	}
	
	
	
	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		log.info("## {}.postSend, sent = {}", this.getClass().getSimpleName(), sent);
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		log.info("## {}.afterSendCompletion, sent = {}", this.getClass().getSimpleName(), sent);
		if (ex != null) {
			log.info("\t > {} : {}", ex.getClass().getSimpleName(), ex.getMessage());
		}
	}
	
}
