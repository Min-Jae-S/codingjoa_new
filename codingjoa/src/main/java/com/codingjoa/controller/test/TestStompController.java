package com.codingjoa.controller.test;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
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
	public void news(@Payload String message, @Header("simpSessionId") String senderSessionId, Principal principal) {
		log.info("## news");
		log.info("\t > senderSessionId = {}", senderSessionId);
		log.info("\t > principal = {}", principal);
		log.info("\t > message = {}", message);

		String sender = getSender(principal);
		String payload = String.format("'%s' 님의 제보입니다: %s", "".equals(sender) ? "익명" : sender, message);

		template.convertAndSend("/sub/news", payload);
	}

	@MessageMapping("/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public ChatMessage chat(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage, 
			@Header("simpSessionId") String senderSessionId, Principal principal) {
		log.info("## chat");
		log.info("\t > senderSessionId = {}", senderSessionId);
		log.info("\t > principal = {}", principal);
		
		String sender = getSender(principal);
		chatMessage.setSender(sender);
		chatMessage.setSenderSessionId(senderSessionId);
		log.info("\t > resolved chatMessage = {}", chatMessage);
		
		return chatMessage;
	}

	private String getSender(Principal principal) {
		if (principal instanceof Authentication) {
			Authentication authentication = (Authentication) principal;
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return principalDetails.getNickname();
		} 
		
		return "";
	}
	
}
