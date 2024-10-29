package com.codingjoa.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.codingjoa.exception.ExceptionMvcHandler;
import com.codingjoa.exception.ExceptionRestHandler;
import com.codingjoa.util.AjaxUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class ExceptionHandlerAspect {
	
	private final ApplicationContext context;
	
	@Pointcut("execution(* com.codingjoa.exception.*.*(..))")
	public void inExceptionHandlerPackage() {}
	
	@Pointcut("@within(org.springframework.web.bind.annotation.ControllerAdvice) || "
			+ "@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
	public void withinControllerAdviceAnnotations() {}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
	public void annotationExceptionHandler() {}
	
	//@Around("annotationExceptionHandler()")
	@Around("inExceptionHandlerPackage() && withinControllerAdviceAnnotations()")
	public Object resolveExceptionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.resolveExceptionHandler", this.getClass().getSimpleName());
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
		
		Object handler = null;
		if (AjaxUtils.isAjaxRequest(request)) {
			log.info("\t > ajax request detected, handling via ExceptionRestHandler");
			handler = context.getBean(ExceptionRestHandler.class);
		} else {
			log.info("\t > non-ajax request detected, handling via ExceptionMvcHandler");
			handler = context.getBean(ExceptionMvcHandler.class);
		}
		
		log.info("\t > resolved handler = {}", handler.getClass().getName());
		return joinPoint.proceed(new Object[] { handler });
	}
}
