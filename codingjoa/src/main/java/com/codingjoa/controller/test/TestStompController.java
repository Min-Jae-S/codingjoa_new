package com.codingjoa.controller.test;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

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
		
		String nickname = (getNickname(principal) == null) ? "익명" : getNickname(principal);
		
		template.convertAndSend("/sub/news", String.format("%s님의 제보입니다: %s", nickname, message));
	}

	@MessageMapping("/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public ChatMessage chat(@DestinationVariable Long roomId, ChatMessage chatMessage, Principal principal) {
		log.info("## chat");
		log.info("\t > chatMessage = {}", chatMessage);
		//log.info("\t > session = {}", session);
		log.info("\t > principal = {}", principal);
		
		String nickname = getNickname(principal);
		
		//chatMessage.setSender(session.getId());
		chatMessage.setSenderNickname(nickname);
		
		return chatMessage;
	}
	
	private String getNickname(Principal principal) {
		if (principal instanceof Authentication) {
			Authentication authentication = (Authentication) principal;
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return principalDetails.getNickname();
		} 
		
		return null;
	}
	
}
