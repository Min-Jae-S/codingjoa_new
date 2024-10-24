package com.codingjoa.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, 
			Exception ex) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		log.info("\t > x-requested-with = {}", request.getHeader("x-requested-with"));
		log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());

		if (handler == null) {
			log.info("\t > handler = {}", handler);
			log.info("\t > handler hasn't been resolved, can't check for @Controller or @RestController");
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
		
		return null;
	}
	
}
