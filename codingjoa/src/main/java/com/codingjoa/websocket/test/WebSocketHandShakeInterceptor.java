package com.codingjoa.websocket.test;

import java.security.Principal;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		log.info("## {}.beforeHandshake", this.getClass().getSimpleName());
		
		Principal principal = request.getPrincipal();
		if (principal == null) {
			String anonymousId = "익명" + RandomStringUtils.randomNumeric(4);
			attributes.put("anonymousId", anonymousId);
			log.info("\t > principal = {}, assigned anonymousId: {}", principal, anonymousId);
		} else {
			log.info("\t > principal = {}, authenticated user: {}", principal.getClass().getSimpleName(), principal.getName());
		}
		
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		log.info("## {}.afterHandshake", this.getClass().getSimpleName());
	}

}
