package com.codingjoa.aop.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class TestAspect {
	
	@Around("execution(* com.codingjoa..*(..))")
	public void test(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.test", this.getClass().getSimpleName());
	}

	@Around("@annotation(AopTest)")
	public void arroundAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.withAnnotation", this.getClass().getSimpleName());
	}
	
	@Before("@annotation(AopTest)")
	public void beforeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.withAnnotation", this.getClass().getSimpleName());
	}
	
	@After("@annotation(AopTest)")
	public void afterAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.afterAnnotation", this.getClass().getSimpleName());
	}
	
}
