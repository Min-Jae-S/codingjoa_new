package com.codingjoa.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.codingjoa.util.AjaxUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnhancedExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {
	
	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod", this.getClass().getSimpleName());
		log.info("\t > hasHandlerMappings = {}", hasHandlerMappings());
		
		HttpServletRequest request = getCurrentHttpRequest();
		log.info("\t > request = {}", request);
		
		if (request != null) {
			log.info("\t > isAjaxRequest = {}", AjaxUtils.isAjaxRequest(request));
		}
		
		ServletInvocableHandlerMethod exceptionHandlerMethod = super.getExceptionHandlerMethod(handlerMethod, exception);
		log.info("\t > exceptionHandlerMethod = {}", exceptionHandlerMethod);
		
//		Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdvices = getExceptionHandlerAdviceCache();
//		log.info("\t > exceptionHandlerAdvices = {}", exceptionHandlerAdvices);
//		
//		if (exceptionHandlerAdvices != null) {
//			exceptionHandlerAdvices.forEach((key, methodResolver) -> log.info("\t\t - {}, {} ", key, methodResolver));
//		}
		
		return exceptionHandlerMethod;
	}
	
	private HttpServletRequest getCurrentHttpRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return (attributes != null) ? attributes.getRequest() : null;
	}
	
}
