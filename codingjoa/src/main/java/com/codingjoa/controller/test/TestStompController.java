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
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.websocket.test.ChatType;
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
	
	@MessageMapping("/chat/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public StompMessage chat(@DestinationVariable Long roomId, @Payload StompMessage stompMessage, 
			@Header("simpSessionId") String senderSessionId, Principal principal) {
		log.info("## chat, roomId = {}", roomId);
		log.info("\t > senderSessionId = {}", senderSessionId);
		log.info("\t > principal = {}", principal);
		
		return stompMessage.toBuilder()
				.type(ChatType.TALK)
				.sender(getSender(principal))
				.senderSessionId(senderSessionId)
				.build();
	}
	
	@SubscribeMapping("/sub/room/{roomId}")
	public String subscription(@DestinationVariable Long roomId) {
		log.info("## subscription, roomId = {}", roomId);
		return "success";
	}
	
	@MessageMapping("/enter/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public StompMessage enter(@DestinationVariable Long roomId, @Header("simpSessionId") String senderSessionId,
			Principal principal) {
		log.info("## enter, roomId = {}", roomId);
		return StompMessage.builder()
				.type(ChatType.ENTER)
				.sender(getSender(principal))
				.senderSessionId(senderSessionId)
				.content("님이 입장하였습니다.")
				.build();
	}

	@MessageMapping("/exit/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public StompMessage exit(@DestinationVariable Long roomId, @Header("simpSessionId") String senderSessionId, 
			Principal principal) {
		log.info("## exit, roomId = {}", roomId);
		return StompMessage.builder()
				.type(ChatType.EXIT)
				.sender(getSender(principal))
				.senderSessionId(senderSessionId)
				.content("님이 퇴장하였습니다.")
				.build();
	}
	
	private String getSender(Principal principal) {
		if (principal instanceof Authentication) {
			Authentication authentication = (Authentication) principal;
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return principalDetails.getNickname();
		} 
		
		return "익명" + RandomStringUtils.randomNumeric(4);
	}
	
}
