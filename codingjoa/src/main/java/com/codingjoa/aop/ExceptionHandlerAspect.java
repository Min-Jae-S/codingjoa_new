package com.codingjoa.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionHandlerAspect {
	
	@Around("execution(* com.example..*(..)) && @within(org.springframework.web.bind.annotation.ControllerAdvice)")
	public Object routeExceptionHandler(ProceedingJoinPoint joinPoint) {
		log.info("## {}.routeExceptionHandler", this.getClass().getSimpleName());
		HttpServletRequest request = null;
		for (Object arg : joinPoint.getArgs()) {
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				break;
			}
		}
		
		if (request != null) {
			if (isAjaxRequest(request)) {
				log.info("\t > handling exception via ExceptionRestHandler");
			} else {
				log.info("\t > handling exception via ExceptionMvcHandler");
			}
		}
		
		return null;
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
	}
}
