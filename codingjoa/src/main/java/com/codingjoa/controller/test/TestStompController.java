package com.codingjoa.controller.test;

import java.security.Principal;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.websocket.test.StompMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestStompController {
	
	private final SimpMessagingTemplate template;
	
	@MessageMapping("/news")
	public void news(@Payload String message, SimpMessageHeaderAccessor accessor, Principal principal) {
		log.info("## news");
		log.info("\t > senderSessionId = {}", accessor.getSessionId());
		log.info("\t > principal = {}", principal);
		log.info("\t > message = {}", message);

		String payload = String.format("'%s' 님의 제보입니다: %s", getSender(principal), message);
		log.info("\t > payload = {}", payload);

		template.convertAndSend("/sub/news", payload);
	}

	@MessageMapping("/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public StompMessage chat(@DestinationVariable Long roomId, @Payload StompMessage stompMessage, 
			@Header("simpSessionId") String senderSessionId, Principal principal) {
		log.info("## chat");
		log.info("\t > senderSessionId = {}", senderSessionId);
		log.info("\t > principal = {}", principal);
		
		stompMessage.setSender(getSender(principal));
		stompMessage.setSenderSessionId(senderSessionId);
		log.info("\t > modified message = {}", stompMessage);
		
		return stompMessage;
	}

	private String getSender(Principal principal) {
		if (principal instanceof Authentication) {
			Authentication authentication = (Authentication) principal;
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return principalDetails.getNickname();
		} 
		
		return "익명" + RandomStringUtils.randomNumeric(6);
	}
	
}
