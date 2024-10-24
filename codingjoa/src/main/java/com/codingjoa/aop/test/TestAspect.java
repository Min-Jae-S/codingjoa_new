package com.codingjoa.aop.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Aspect
@Component
public class TestAspect {
	
	@Before("@annotation(com.codingjoa.annotation.AnnoTest)")
	public void annotation() throws Throwable {
		log.info("## @annotation", this.getClass().getSimpleName());
	}

	@Before("@within(com.codingjoa.annotation.AnnoTest)")
	public void within() throws Throwable {
		log.info("## @within", this.getClass().getSimpleName());
	}

//	@Before("@within(com.codingjoa.annotation.AnnoTest) || @annotation(com.codingjoa.annotation.AnnoTest)")
//	public void withinOrAnnotation() throws Throwable {
//		log.info("## @within || @annotation", this.getClass().getSimpleName());
//	}
	
//	@After("@annotation(com.codingjoa.annotation.AnnoTest)")
//	public void afterAnnotation() throws Throwable {
//		log.info("## {}.afterAnnotation", this.getClass().getSimpleName());
//	}
	
//	@Around("@annotation(com.codingjoa.annotation.AnnoTest)")
//	public Object arroundAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
//		log.info("## {}.arroundAnnotation", this.getClass().getSimpleName());
//		log.info("\t > joinPoint = {}", joinPoint);
//		return joinPoint.proceed();
//	}
	
//	@Before("@annotation(com.codingjoa.annotation.AspecAnnoTesttTest)")
//	//@Before("execution(* com.codingjoa.controller.test.*.*(..))")
//	//@Before("execution(* com.codingjoa.controller.test.*Controller.*.*(..))")
//	public void beforeController() throws Throwable {
//		log.info("## {}.beforeController", this.getClass().getSimpleName());
//	}

//	@Around("execution(* com.codingjoa.service.test.TestProxyService.*(..))")
//	public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
//		log.info("## {}.aroundService", this.getClass().getSimpleName());
//		return joinPoint.proceed();
//	}

	
}
