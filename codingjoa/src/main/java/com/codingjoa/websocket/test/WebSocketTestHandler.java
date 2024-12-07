package com.codingjoa.websocket.test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codingjoa.dto.SuccessResponse;
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
		
		for (WebSocketSession webSocketSession : sessions) {
			String greeting = String.format("%s 님이 입장하셨습니다.", session.getId());
			webSocketSession.sendMessage(new TextMessage(greeting));
		}
		
		log.info("\t > current sessions = {}", getCurrrentSessions());
		log.info("\t > my session id = {}", session.getId());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("## {}.afterConnectionClosed", this.getClass().getSimpleName());
		sessions.remove(session);
		log.info("\t > current sessions = {}", getCurrrentSessions());
		log.info("\t > my session id = {}", session.getId());
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("## {}.handleTextMessage", this.getClass().getSimpleName());

		String id = session.getId();
		String payload = message.getPayload();
		log.info("\t > payload = {}", payload);
		
		TestWebSocketReponse chat = TestWebSocketReponse.builder()
				.id(id)
				.chatMessage(payload)
				.build();
		
		SuccessResponse response = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.data(chat)
				.message("success")
				.build();
		
		String json = objectMapper.writeValueAsString(response);
		
		for (WebSocketSession webSocketSession : sessions) {
			webSocketSession.sendMessage(new TextMessage(json));
		}
	}
	
	public void sendNoticiation(String message) {
		log.info("## sendNoticiation");
		log.info("\t > current sessions = {}", getCurrrentSessions());
		
		for (WebSocketSession session : sessions) {
			if (session.isOpen()) {
				try {
					session.sendMessage(new TextMessage(message));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Set<String> getCurrrentSessions() {
		return sessions.stream()
				.map(webSocketSession -> webSocketSession.getId())
				.collect(Collectors.toSet());
	}
	
}
