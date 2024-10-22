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
	
	@Before("execution(* com.codingjoa.controller.test.*Controller.*(..))")
	public void beforeController() throws Throwable {
		System.out.println("## beforeController");
		log.info("## {}.beforeController", this.getClass().getSimpleName());
	}

	@Around("@annotation(com.codingjoa.annotation.AspectTest)")
	public void arroundAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.arroundAnnotation", this.getClass().getSimpleName());
		log.info("\t > joinPoint = {}", joinPoint);
	}
	
	@Before("@annotation(com.codingjoa.annotation.AspectTest)")
	public void beforeAnnotation() throws Throwable {
		log.info("## {}.beforeAnnotation", this.getClass().getSimpleName());
	}
	
	@After("@annotation(com.codingjoa.annotation.AspectTest)")
	public void afterAnnotation() throws Throwable {
		log.info("## {}.afterAnnotation", this.getClass().getSimpleName());
	}
	
}
