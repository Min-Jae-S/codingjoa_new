package com.codingjoa.controller.test;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.codingjoa.websocket.test.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestStompController {
	
	@SuppressWarnings("unused")
	private final SimpMessagingTemplate template;
	
	@MessageMapping("/room/{roomId}") // /pub/room/{roomId}
	public void chat(ChatMessage chatMessage) {
		log.info("## chat");
		log.info("\t > {}", chatMessage);
	}
	
//	@MessageMapping("/room/{roomId}")
//	//@SendTo("/sub/room/{roomId}")
//	public ChatMessage chat(@DestinationVariable Long roomId, ChatMessage chatMessage, 
//			@AuthenticationPrincipal PrincipalDetails principal, WebSocketSession session) {
//		log.info("## chat");
//		log.info("\t > {}", chatMessage);
//		log.info("\t > session = {}", session);
//		
//		String senderNickname = (principal != null) ? principal.getNickname() : null;
//		chatMessage.setSender(session.getId());
//		chatMessage.setSenderNickname(senderNickname);
//		log.info("\t > {}", chatMessage);
//		
//		return chatMessage;
//	}
	
}
