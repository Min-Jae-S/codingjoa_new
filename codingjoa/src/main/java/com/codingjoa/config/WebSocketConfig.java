package com.codingjoa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.codingjoa.websocket.WebSocketNotificationHandler;
import com.codingjoa.websocket.test.WebSocketTestHandler;
import com.codingjoa.websocket.test.WebSocketTestInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ComponentScan("com.codingjoa.websocket")
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final WebSocketTestHandler testHandler;
	private final WebSocketNotificationHandler notificationHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		log.info("## registerWebSocketHandlers");
		registry.addHandler(testHandler, "/test/ws/socket")
				.addInterceptors(new WebSocketTestInterceptor())
				.setAllowedOrigins("*");
		registry.addHandler(notificationHandler, "/ws")
				.setAllowedOrigins("*");
	}
	
}
