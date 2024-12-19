package com.codingjoa.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.codingjoa.websocket.test.StompChannelInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
	
	private final ObjectMapper objectMapper;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-stomp").setAllowedOrigins("*");
		//registry.addEndpoint("/ws/stomp").setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//registry.enableSimpleBroker("/queue", "/topic");		// to message broker, queue(1:1), topic(1:N)
		registry.enableSimpleBroker("/sub");
		registry.setApplicationDestinationPrefixes("/pub"); 	// to @MessageMapping(handler)
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new StompChannelInterceptor(objectMapper));
	}

	// CompositeMessageConverter
	// https://stackoverflow.com/questions/41604828/how-to-use-and-customize-messageconversionspring-websocket-client
	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		log.info("## configureMessageConverters");
		messageConverters.add(new StringMessageConverter());

		MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();
		jsonConverter.setObjectMapper(objectMapper);
		messageConverters.add(jsonConverter);
		messageConverters.forEach(converter -> log.info("\t > {}", converter.getClass().getSimpleName()));
		
		return true;
	}

}
