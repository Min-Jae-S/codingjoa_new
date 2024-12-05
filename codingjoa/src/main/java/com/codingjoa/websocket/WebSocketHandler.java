package com.codingjoa.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
	
	@SuppressWarnings("unused")
	private final ObjectMapper objectMapper;
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		log.info("## {}.afterConnectionEstablished", this.getClass().getSimpleName());
		super.handleMessage(session, message);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("## {}.afterConnectionEstablished", this.getClass().getSimpleName());
		super.afterConnectionEstablished(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("## {}.afterConnectionClosed", this.getClass().getSimpleName());
		super.afterConnectionClosed(session, status);
	}

	
}
