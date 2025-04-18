package com.codingjoa.config.servlet;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.codingjoa.websocket.test.WebSocketHandler;
import com.codingjoa.websocket.test.WebSocketHandShakeInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ComponentScan("com.codingjoa.websocket")
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final WebSocketHandler webSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/ws")
				.addInterceptors(new WebSocketHandShakeInterceptor())
				.setAllowedOrigins("*");
	}
	
	
	
}
