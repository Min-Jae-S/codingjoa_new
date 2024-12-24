package com.codingjoa.websocket.test;

import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
		
		// outbound: CONNECT_ACK, DISCONNECT_ACK, MESSAGE, ERROR
		if (messageType == SimpMessageType.MESSAGE) {
			Object payload = message.getPayload();
			if (payload instanceof byte[]) {
				try {
					ChatMessage chatMessage = objectMapper.readValue((byte[]) payload, ChatMessage.class);
					
					String senderSessionId = chatMessage.getSender();
					String receiverSessionId = accessor.getSessionId();
					chatMessage.setSessionMatched(receiverSessionId.equals(senderSessionId));
					
					ObjectWriter writer = objectMapper.writerFor(ChatMessage.class)
							.withoutAttribute("senderSessionId");
					byte[] modifiedPayload = writer.writeValueAsBytes(chatMessage);
					log.info("\t > modified payload: {}", FormatUtils.formatPrettyJson(chatMessage));
					
					return MessageBuilder.createMessage(modifiedPayload, accessor.getMessageHeaders());
				} catch (Exception e) {
					String decodedPayload = new String((byte[]) payload, StandardCharsets.UTF_8);
					log.info("\t > payload: {}", decodedPayload);
				}
			}
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
