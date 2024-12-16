package com.codingjoa.controller.test;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.websocket.test.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Controller
public class TestStompController {
	
	private final SimpMessagingTemplate template;
	
	@MessageMapping("/{roomNumber}") // /send/5
	@SendTo("/topic")
	public ChatMessage test(@DestinationVariable Long rommNumber, ChatMessage chatMessage, 
			@AuthenticationPrincipal PrincipalDetails principal, WebSocketSession session) {
		log.info("## test");
		log.info("\t > {}", chatMessage);
		log.info("\t > session = {}", session);
		
		String senderNick = (principal != null) ? principal.getNickname() : null;
		chatMessage.setSender(session.getId());
		chatMessage.setSenderNickname(senderNick);
		log.info("\t > {}", chatMessage);
		
		return chatMessage;
	}
	
}
