package com.codingjoa.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.codingjoa.util.AjaxUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionHandlerAspect {
	
	@Pointcut("execution(* com.codingjoa.exception.*.*(..))")
	public void inExceptionHandlerLayer() {}
	
	@Pointcut("@within(org.springframework.web.bind.annotation.ControllerAdvice) || "
			+ "@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
	public void withinControllerAdviceAnnotations() {}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
	public void anootationExceptionHandler() {}
	
	@Before("anootationExceptionHandler()")
	public void beforeExceptionHandler() {
		log.info("# {}.beforeExceptionHandler", this.getClass().getSimpleName());
	}
	
	@Around("inExceptionHandlerLayer() && withinControllerAdviceAnnotations()")
	public Object routeExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.routeExceptionHandler", this.getClass().getSimpleName());
		log.info("\t > target = {}", joinPoint.getTarget().getClass().getSimpleName());
		
		HttpServletRequest request = null;
		for (Object arg : joinPoint.getArgs()) {
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				break;
			}
		}
		
		if (request == null) {
			log.info("\t > no HttpServletRequest instance in the arguments");
			// throw exception..
		}
		
		if (AjaxUtils.isAjaxRequest(request)) {
			log.info("\t > ajax request detected, handling via ExceptionRestHandler");
			return joinPoint.proceed();
		} else {
			log.info("\t > non-ajax request detected, handling via ExceptionMvcHandler");
			return joinPoint.proceed();
		}
	}
}
