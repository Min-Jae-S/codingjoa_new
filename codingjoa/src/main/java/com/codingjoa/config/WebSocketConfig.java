package com.codingjoa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.codingjoa.websocket.WebSocketInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ComponentScan("com.codingjoa.websocket")
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final WebSocketHandler webSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		log.info("## registerWebSocketHandlers");
		registry.addHandler(webSocketHandler, "/ws", "/test/ws/*")
				.addInterceptors(new WebSocketInterceptor())
				.setAllowedOrigins("*");
	}

}
