package com.codingjoa.exception;

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

		if (handler == null) {
			log.info("\t > handler = {}", handler);
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			int beginIndex = handlerMethod.toString().lastIndexOf(".") + 1;
			log.info("\t > handler = {}", handlerMethod.toString().substring(beginIndex));
//			Class<?> controller = handlerMethod.getBeanType();
//			if (controller.isAnnotationPresent(Controller.class)) {
//				log.info("\t > handler = @Controller, {}", handlerMethod.toString().substring(beginIndex));
//			} else if (controller.isAnnotationPresent(RestController.class)) {
//				log.info("\t > handler = @RestController, {}", handlerMethod.toString().substring(beginIndex));
//			}
		} else {
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}
		
		return null;
	}
	
}