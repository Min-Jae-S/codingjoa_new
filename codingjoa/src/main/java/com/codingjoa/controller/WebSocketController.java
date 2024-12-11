package com.codingjoa.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketController {

	private final SimpMessagingTemplate template;
	
	@MessageMapping("/ws/stomp/test")
	public void test() {
		
	}
	
}
