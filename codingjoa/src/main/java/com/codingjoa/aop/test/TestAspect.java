package com.codingjoa.aop.test;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//@SuppressWarnings("unused")
@Slf4j
@Aspect
@Component
public class TestAspect {
	
	@Before("@annotation(com.codingjoa.annotation.AspectTest)")
	//@Before("execution(* com.codingjoa.controller.test.*.*(..))")
	//@Before("execution(* com.codingjoa.controller.test.*Controller.*.*(..))")
	public void beforeController() throws Throwable {
		log.info("## {}.beforeController", this.getClass().getSimpleName());
	}

//	@Around("execution(* com.codingjoa.service.test.TestProxyService.*(..))")
//	public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
//		log.info("## {}.aroundService", this.getClass().getSimpleName());
//		return joinPoint.proceed();
//	}

//	@Around("@annotation(com.codingjoa.annotation.AspectTest)")
//	public Object arroundAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
//		log.info("## {}.arroundAnnotation", this.getClass().getSimpleName());
//		log.info("\t > joinPoint = {}", joinPoint);
//		return joinPoint.proceed();
//	}
	
	@Before("@annotation(com.codingjoa.annotation.AspectTest)")
	public void beforeAnnotation() throws Throwable {
		log.info("## {}.beforeAnnotation", this.getClass().getSimpleName());
	}
	
//	@After("@annotation(com.codingjoa.annotation.AspectTest)")
//	public void afterAnnotation() throws Throwable {
//		log.info("## {}.afterAnnotation", this.getClass().getSimpleName());
//	}
	
}
