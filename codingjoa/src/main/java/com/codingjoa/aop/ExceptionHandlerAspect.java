package com.codingjoa.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionHandlerAspect {
	
	@Around("execution(* com.codingjoa..*(..)) && "
			+ "@within(org.springframework.web.bind.annotation.ControllerAdvice) && "
			+ "@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
	public Object routeExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.routeExceptionHandler", this.getClass().getSimpleName());
		HttpServletRequest request = null;
		for (Object arg : joinPoint.getArgs()) {
			log.info("\t > arg = {}", (arg == null) ? null : arg.getClass().getSimpleName());
			
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				break;
			}
		}
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		
		if (request != null) {
			if (isAjaxRequest(request)) {
				log.info("\t > handling exception via ExceptionRestHandler");
				return joinPoint.proceed();
			} else {
				log.info("\t > handling exception via ExceptionMvcHandler");
				return joinPoint.proceed();
			}
		}
		
		return null;
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
	}
}
