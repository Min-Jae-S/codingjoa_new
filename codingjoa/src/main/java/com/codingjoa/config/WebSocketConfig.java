package com.codingjoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		log.info("## registerStompEndpoints");
		WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		log.info("## configureMessageBroker");
		WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
	}

}
