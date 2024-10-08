package com.codingjoa.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, 
			Exception ex) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());

		if (handler == null) {
			log.info("\t > handler is not resolved yet");
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			int index = handlerMethod.toString().lastIndexOf(".");
			log.info("\t > handler = {}", handlerMethod.toString().substring(index + 1));
		} else {
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}
		return null;
	}
	
}
