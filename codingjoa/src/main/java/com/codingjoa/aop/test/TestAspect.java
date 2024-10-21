package com.codingjoa.aop.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class TestAspect {
	
	@Around("execution(* com.codingjoa..*(..))")
	public Object test(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.test", this.getClass().getSimpleName());
		return null;
	}
	
}
