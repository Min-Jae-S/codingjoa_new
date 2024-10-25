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
	
	@Around("execution(* com.codingjoa.exception.*.*(..)) && @target(org.springframework.web.bind.annotation.ControllerAdvice)")
	public Object routeExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.routeExceptionHandler", this.getClass().getSimpleName());
		log.info("\t > signature = {}", joinPoint.getSignature());
		
		HttpServletRequest request = null;
		for (Object arg : joinPoint.getArgs()) {
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				break;
			}
		}
		
		if (request != null) {
			if (isAjaxRequest(request)) {
				log.info("\t > ajax request, exception should be handled by ExceptionRestHandler");
				return joinPoint.proceed();
			} else {
				log.info("\t > not ajax request, exception should be handled by ExceptionMvcHandler");
				return joinPoint.proceed();
			}
		}
		
		return joinPoint.proceed();
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
	}
}
