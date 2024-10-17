package com.codingjoa.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionHandlerAspect {
	
	public void routeExceptionHandler(ProceedingJoinPoint joinPoint) {
		log.info("## {}.routeExceptionHandler", this.getClass().getSimpleName());
	}
}
