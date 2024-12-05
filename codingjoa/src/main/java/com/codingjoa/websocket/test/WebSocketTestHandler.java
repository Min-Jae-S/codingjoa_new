package com.codingjoa.websocket.test;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketTestHandler extends TextWebSocketHandler {
	
	private final Set<WebSocketSession> participants = ConcurrentHashMap.newKeySet(); // thread-safe
	private final ObjectMapper objectMapper;
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("## {}.handleTextMessage", this.getClass().getSimpleName());
		log.info("\t > payload = {}", message.getPayload());
		super.handleTextMessage(session, message);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("## {}.afterConnectionEstablished", this.getClass().getSimpleName());
		log.info("\t > webSocketSession = {}", session);
		
		for (Entry<String, Object> entry : session.getAttributes().entrySet()) {
			log.info("\t > key = {}, value = {}", entry.getKey(), entry.getValue());
		}
		
		participants.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("## {}.afterConnectionClosed", this.getClass().getSimpleName());
		participants.remove(session);
	}

	
}
