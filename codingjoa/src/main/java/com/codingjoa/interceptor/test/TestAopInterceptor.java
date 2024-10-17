package com.codingjoa.interceptor.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestAopInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		log.info("## {}.postHandle", this.getClass().getSimpleName());

		if (handler == null) {
			log.info("\t > handler = {}", handler);
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			int beginIndex = handlerMethod.toString().lastIndexOf(".") + 1;
			log.info("\t > handler = {}", handlerMethod.toString().substring(beginIndex));
			
			Class<?> controller = handlerMethod.getBeanType();
			if (controller.isAnnotationPresent(Controller.class)) {
				log.info("\t > controller = @Controller");
			} else if (controller.isAnnotationPresent(RestController.class)) {
				log.info("\t > controller = @RestController");
			}
		} else {
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}
		
		throw new RuntimeException();
	}
}
