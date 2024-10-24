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
	
	@Around("execution(* com.codingjoa.exception.*.*(..)) && @within(org.springframework.web.bind.annotation.ControllerAdvice)")
	public Object routeExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.routeExceptionHandler", this.getClass().getSimpleName());
		log.info("\t > joinPoint = {}", joinPoint);
		
		HttpServletRequest request = null;
		for (Object arg : joinPoint.getArgs()) {
			log.info("\t > arg = {}", (arg == null) ? null : arg.getClass().getSimpleName());
			
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
				break;
			}
		}
		
		if (request != null) {
			if (isAjaxRequest(request)) {
				log.info("\t > exception will be handled by ExceptionRestHandler");
				return joinPoint.proceed();
			} else {
				log.info("\t > exception will be handled by ExceptionMvcHandler");
				return joinPoint.proceed();
			}
		}
		
		return joinPoint.proceed();
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
	}
}
