package com.codingjoa.websocket.test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codingjoa.quartz.AlarmDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketTestHandler extends TextWebSocketHandler {
	
	private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet(); // thread-safe
	private final ObjectMapper objectMapper;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("## {}.afterConnectionEstablished", this.getClass().getSimpleName());
		sessions.add(session);
		log.info("\t > current sessions = {}", getCurrrentSessions());
		log.info("\t > session id = {}, pricipal = {}", session.getId(), session.getPrincipal());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("## {}.afterConnectionClosed", this.getClass().getSimpleName());
		sessions.remove(session);
		log.info("\t > current sessions = {}", getCurrrentSessions());
		log.info("\t > session id = {}, pricipal = {}", session.getId(), session.getPrincipal());
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("## {}.handleTextMessage", this.getClass().getSimpleName());
		log.info("\t > session id = {}, pricipal = {}", session.getId(), session.getPrincipal());

		TestWebSocketReponse response = TestWebSocketReponse.builder()
				.id(session.getId())
				.chatMessage(message.getPayload())
				.build();
		log.info("\t > response = {}", response);
		
		String responseJson = objectMapper.writeValueAsString(response);
		
		for (WebSocketSession webSocketSession : sessions) {
			webSocketSession.sendMessage(new TextMessage(responseJson));
		}
	}
	
	public void sendNoticiation(AlarmDto alarmDto) {
		log.info("## sendNoticiation");
		try {
			String json = objectMapper.writeValueAsString(alarmDto);
			for (WebSocketSession session : sessions) {
				if (session.isOpen()) {
					session.sendMessage(new TextMessage(json));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Set<String> getCurrrentSessions() {
		return sessions.stream()
				.map(webSocketSession -> webSocketSession.getId())
				.collect(Collectors.toSet());
	}
	
}
