package com.codingjoa.error;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.codingjoa.util.RequestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	private final ExceptionHandlerExceptionResolver baseResolver;

	public PreExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver baseResolver) {
		this.baseResolver = baseResolver;
	}
	
	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception exception) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > handlerMethod = {}", handlerMethod);
		
		if (handlerMethod == null) {
			log.info("\t > exception will be handled by this resolver: {}", this.getClass().getSimpleName());
			return super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
		}
		
		log.info("\t > passing control to the next resolver: {}", baseResolver.getClass().getSimpleName());
		return null;
	}

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		HttpServletRequest request = getCurrentHttpRequest();
		if (request == null) {
			return null;
		}

		boolean isJsonRequest = RequestUtils.isJsonRequest(request);
		
		for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : getExceptionHandlerAdviceCache().entrySet()) {
			ControllerAdviceBean advice = entry.getKey();
			boolean isRestControllerAdvice = advice.getBeanType().isAnnotationPresent(RestControllerAdvice.class);
			if (isJsonRequest == isRestControllerAdvice) {
				log.info("\t > isJsonRequest: {}, matched advice: {}", isJsonRequest, advice);
				ExceptionHandlerMethodResolver resolver = entry.getValue();
				Method method = resolver.resolveMethod(exception);
				if (method != null) {
					return new ServletInvocableHandlerMethod(advice.resolveBean(), method);
				}
			}
		}

		return null;
	}

	@Override
	public void afterPropertiesSet() {
		this.setApplicationContext(baseResolver.getApplicationContext());
		this.setMessageConverters(baseResolver.getMessageConverters());
		// WebMvcConfigurationSupport.addDefaultHandlerExceptionResolvers()
		this.setResponseBodyAdvice(Collections.singletonList(new JsonViewResponseBodyAdvice()));
		super.afterPropertiesSet();
	}

	private HttpServletRequest getCurrentHttpRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return (attributes != null) ? attributes.getRequest() : null;
	}

}
