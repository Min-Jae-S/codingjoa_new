package com.codingjoa.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.codingjoa.util.AjaxUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EnhancedExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod", this.getClass().getSimpleName());
		
		HttpServletRequest request = getCurrentHttpRequest();
		log.info("\t > request = {}", request);
		
		if (request != null) {
			log.info("\t > isAjaxRequest = {}", AjaxUtils.isAjaxRequest(request));
		}
		
		ServletInvocableHandlerMethod exceptionHandlerMethod = super.getExceptionHandlerMethod(handlerMethod, exception);
		log.info("\t > exceptionHandlerMethod = {}", exceptionHandlerMethod);
		
		Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdvices = getExceptionHandlerAdviceCache();
		log.info("\t > exceptionHandlerAdvices = {}", exceptionHandlerAdvices);
		
		if (exceptionHandlerAdvices != null) {
			exceptionHandlerAdvices.forEach((key, methodResolver) -> log.info("\t\t - {}, {} ", key, methodResolver));
		}
		
		log.info("\t > resolvers = {}", getArgumentResolvers().getResolvers());
		log.info("\t > retrunValueHandlers = {}", getReturnValueHandlers().getHandlers());
		
		return exceptionHandlerMethod;
	}
	
	private HttpServletRequest getCurrentHttpRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return (attributes != null) ? attributes.getRequest() : null;
	}
	
}
