package com.codingjoa.controller.test;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.websocket.test.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestStompController {
	
	private final SimpMessagingTemplate template;
	
	@MessageMapping("/news")
	public void news(@Payload String message, Principal principal) {
		log.info("## news");
		log.info("\t > message = {}", message);
		log.info("\t > principal = {}", principal);
		
		String nickname = null;
		if (principal instanceof Authentication) {
			Authentication authentication = (Authentication) principal;
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			nickname = principalDetails.getNickname();
		} else {
			nickname = "익명";
		}
		
		template.convertAndSend("/sub/news", String.format("%s님의 제보입니다: %s", nickname, message));
	}

	@MessageMapping("/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public ChatMessage chat(@DestinationVariable Long roomId, ChatMessage chatMessage, 
			@AuthenticationPrincipal PrincipalDetails principal, WebSocketSession session) {
		log.info("## chat");
		log.info("\t > chatMessage = {}", chatMessage);
		log.info("\t > session = {}", session);
		
		String nickname = (principal != null) ? principal.getNickname() : null;
		chatMessage.setSender(session.getId());
		chatMessage.setSenderNickname(nickname);
		
		return chatMessage;
	}
	
}
