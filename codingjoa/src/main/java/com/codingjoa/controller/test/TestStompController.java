package com.codingjoa.controller.test;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
	
	@MessageMapping("/broadcast")
	public void broadcast(@Payload String message, SimpMessageHeaderAccessor accessor, Principal principal) {
		log.info("## broadcast");
		log.info("\t > message = {}", message);
		
		String sender = getSender(principal, accessor);
//		StompMessage payload = StompMessage.builder()
//				.type(ChatType.BROADCAST)
//				.sender(sender)
//				.senderSessionId(accessor.getSessionId())
//				.content(message)
//				.build();
//		template.convertAndSend("/sub/broadcast", payload);
		
		String payload = String.format("[broadcast] %s: %s", sender, message);
		template.convertAndSend("/sub/broadcast", payload);
	}
	
	@MessageMapping("/chat/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public StompMessage chat(@DestinationVariable Long roomId, @Payload StompMessage stompMessage, 
			/*@Header("simpSessionId") String senderSessionId,*/ SimpMessageHeaderAccessor accessor, Principal principal) {
		log.info("## chat, roomId = {}", roomId);
		return stompMessage.toBuilder()
				.type(ChatType.TALK)
				.sender(getSender(principal, accessor))
				.senderSessionId(accessor.getSessionId())
				.build();
	}
	
	@MessageMapping("/enter/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public StompMessage enter(@DestinationVariable Long roomId, SimpMessageHeaderAccessor accessor, Principal principal) {
		log.info("## enter, roomId = {}", roomId);
		return StompMessage.builder()
				.type(ChatType.ENTER)
				.sender(getSender(principal, accessor))
				.senderSessionId(accessor.getSessionId())
				.content("님이 입장하였습니다.")
				.build();
	}

	@MessageMapping("/exit/room/{roomId}")
	@SendTo("/sub/room/{roomId}")
	public StompMessage exit(@DestinationVariable Long roomId, SimpMessageHeaderAccessor accessor, Principal principal) {
		log.info("## exit, roomId = {}", roomId);
		return StompMessage.builder()
				.type(ChatType.EXIT)
				.sender(getSender(principal, accessor))
				.senderSessionId(accessor.getSessionId())
				.content("님이 퇴장하였습니다.")
				.build();
	}
	
	private String getSender(Principal principal, SimpMessageHeaderAccessor accessor) {
		if (principal instanceof Authentication) {
			Authentication authentication = (Authentication) principal;
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return principalDetails.getNickname();
		}
		
		return (String) accessor.getSessionAttributes().get("anonymousId");
	}
	
}
