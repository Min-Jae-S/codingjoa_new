package com.codingjoa.websocket.test;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codingjoa.quartz.AlarmDto;
import com.codingjoa.security.dto.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
	
	//private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet(); // thread-safe
	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	private final ObjectMapper objectMapper;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("## {}.afterConnectionEstablished", this.getClass().getSimpleName());
		
		String sessionId = session.getId();
		sessions.put(sessionId, session);
		
		Message message = Message.builder()
				.type("enter")
				.sender(sessionId)
				.senderNickname(getSenderNickname(session))
				.build();
		log.info("\t > message = {}", message);
		
		String json = objectMapper.writeValueAsString(message);
		
		sessions.values().forEach(s -> {
			// no need to send the connection info to oneself, so it will be sent to all other sessions except for oneself
			if (!s.getId().equals(sessionId)) {
				try {
					s.sendMessage(new TextMessage(json));
				} catch (IOException e) {
					// throw e
				}
			}
		});
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("## {}.afterConnectionClosed", this.getClass().getSimpleName());
		
		String sessionId = session.getId();
		sessions.remove(sessionId);
		
		Message message = Message.builder()
				.type("exit")
				.sender(sessionId)
				.senderNickname(getSenderNickname(session))
				.build();
		log.info("\t > message = {}", message);
		
		String json = objectMapper.writeValueAsString(message);
		
		sessions.values().forEach(s -> {
			if (!s.getId().equals(sessionId)) {
				try {
					s.sendMessage(new TextMessage(json));
				} catch (IOException e) {
					// throw e
				}
			}
		});
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
		log.info("## {}.handleTextMessage", this.getClass().getSimpleName());
		Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);

		String sessionId = session.getId();
		message.setSender(sessionId);
		message.setSenderNickname(getSenderNickname(session));
		log.info("\t > message = {}", message);
		
		String json = objectMapper.writeValueAsString(message);
		
		sessions.values().forEach(s -> {
			if (!s.getId().equals(sessionId)) {
				try {
					s.sendMessage(new TextMessage(json));
				} catch (IOException e) {
					// throw e
				}
			}
		});
	}
	
	public void sendAlarm(AlarmDto alarmDto) throws Exception {
		log.info("## sendAlarm");
		String json = objectMapper.writeValueAsString(alarmDto);
		
		sessions.values().forEach(s -> {
			try {
				s.sendMessage(new TextMessage(json));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private String getSenderNickname(WebSocketSession session) {
		Principal principal = session.getPrincipal();
		return (principal == null) ? null : ((PrincipalDetails) principal).getNickname();
	}

}
