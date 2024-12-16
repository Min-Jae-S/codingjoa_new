package com.codingjoa.controller.test;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
	public ChatMessage test(@DestinationVariable Long rommNumber, ChatMessage chatMessage) {
		log.info("## test");
		return ChatMessage.builder().build();
	}
	
}
