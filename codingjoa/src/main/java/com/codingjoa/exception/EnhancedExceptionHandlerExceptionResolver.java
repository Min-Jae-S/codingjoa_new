package com.codingjoa.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
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
	
	@Override
	public void afterPropertiesSet() {
		log.info("## {}.afterPropertiesSet", this.getClass().getSimpleName());
		initExceptionHandlerAdviceCache();
		super.afterPropertiesSet();
	}
	
	private void initExceptionHandlerAdviceCache() {
		ApplicationContext context = getApplicationContext();
		log.info("\t > context = {}", context);
		
		if (context == null) {
			return;
		}

		List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
		log.info("\t > adviceBeans = {}", adviceBeans);
		
		for (ControllerAdviceBean adviceBean : adviceBeans) {
			Class<?> beanType = adviceBean.getBeanType();
			log.info("\t\t - beanType = {}", beanType);
			if (beanType == null) {
				throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
			}
			ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
			log.info("\t\t - hasExceptionMappings = {}", resolver.hasExceptionMappings());
			log.info("\t\t - ResponseBodyAdvice isAssignableFrom = {}", ResponseBodyAdvice.class.isAssignableFrom(beanType));
		}
		
		log.info("\t > handlerSize = {}", getExceptionHandlerAdviceCache().size());
	}

	private HttpServletRequest getCurrentHttpRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return (attributes != null) ? attributes.getRequest() : null;
	}
	
}
