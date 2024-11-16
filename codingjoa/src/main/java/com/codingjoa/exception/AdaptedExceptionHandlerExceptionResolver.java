package com.codingjoa.exception;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.codingjoa.util.AjaxUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdaptedExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	private final ExceptionHandlerExceptionResolver baseResolver;

	public AdaptedExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver baseResolver) {
		this.baseResolver = baseResolver;
	}

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod", this.getClass().getSimpleName());
		log.info("\t > handlerMethod = {}", handlerMethod);

		ServletInvocableHandlerMethod invocableHandlerMethod = super.getExceptionHandlerMethod(handlerMethod, exception);
		log.info("\t > invocableHandlerMethod from super = {}", invocableHandlerMethod);
		log.info("\t > exceptionHandlerAdviceCache super = {}", super.getExceptionHandlerAdviceCache());

		if (handlerMethod == null) {
			HttpServletRequest request = getCurrentHttpRequest();
			log.info("\t > request = {}", request);

			if (request == null) {
				return null;
			}

			boolean isAjaxRequest = AjaxUtils.isAjaxRequest(request);

			for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : getExceptionHandlerAdviceCache()
					.entrySet()) {
				ControllerAdviceBean advice = entry.getKey();
				boolean isRestControllerAdvice = advice.getBeanType().isAnnotationPresent(RestControllerAdvice.class);
				if (isAjaxRequest == isRestControllerAdvice) {
					ExceptionHandlerMethodResolver resolver = entry.getValue();
					Method method = resolver.resolveMethod(exception);
					if (method != null) {
						return new ServletInvocableHandlerMethod(advice.resolveBean(), method);
					}
				}
			}
		}

		return invocableHandlerMethod;
	}

	@Override
	public void afterPropertiesSet() {
		this.setApplicationContext(baseResolver.getApplicationContext());
		this.setMessageConverters(baseResolver.getMessageConverters());
		// WebMvcConfigurationSupport#addDefaultHandlerExceptionResolvers
		this.setResponseBodyAdvice(Collections.singletonList(new JsonViewResponseBodyAdvice()));
		super.afterPropertiesSet();
	}

	private HttpServletRequest getCurrentHttpRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return (attributes != null) ? attributes.getRequest() : null;
	}

}
