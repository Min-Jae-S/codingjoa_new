package com.codingjoa.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.codingjoa.util.AjaxUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class ExceptionResolverAspect {
	
	//private final ApplicationContext context;
	private final WebApplicationContext context;
	
	@Pointcut("execution(* com.codingjoa.exception.*.*(..))")
	public void inExceptionHandlerPackage() {}
	
	@Pointcut("@within(org.springframework.web.bind.annotation.ControllerAdvice) || "
			+ "@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
	public void withinControllerAdviceAnnotations() {}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
	public void annotationExceptionHandler() {}

	@Pointcut("execution(* org.springframework.web.servlet.HandlerExceptionResolver.resolveException(..))")
	public void resolveExceptionByHandlerExceptionResolver() {}

	@Pointcut("execution(* org.springframework.web.servlet.handler.HandlerExceptionResolverComposite.resolveException(..))")
	public void resolveExceptionByHandlerExceptionResolverComposite() {}

	@Pointcut("execution(* org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver.resolveException(..))")
	public void resolveExceptionByExceptionHandlerExceptionResolver() {}
	
	/*
	public class HandlerExceptionResolverComposite implements HandlerExceptionResolver, Ordered {
		
		@Nullable
		private List<HandlerExceptionResolver> resolvers;
		private int order = Ordered.LOWEST_PRECEDENCE;
		
		@Override
		@Nullable
		public ModelAndView resolveException(
				HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
	
			if (this.resolvers != null) {
				for (HandlerExceptionResolver handlerExceptionResolver : this.resolvers) {
					ModelAndView mav = handlerExceptionResolver.resolveException(request, response, handler, ex);
					if (mav != null) {
						return mav;
					}
				}
			}
			return null;
		}
	}
	*/
	
	//@Around("resolveExceptionByHandlerExceptionResolver()")
	//@Around("resolveExceptionByHandlerExceptionResolverComposite()")
	@Around("resolveExceptionByExceptionHandlerExceptionResolver()")
	public Object resolveWithDynamicResolver(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.resolveWithDynamicResolver", this.getClass().getSimpleName());
		log.info("\t > target = {}", joinPoint.getTarget().getClass().getSimpleName());
		
		Object[] args = joinPoint.getArgs();
		Object handler = args[2];
		log.info("\t > handler = {}", handler);
		
		if (handler != null) {
			return joinPoint.proceed();
		}
		
		HttpServletRequest request = (HttpServletRequest) args[0];
		if (AjaxUtils.isAjaxRequest(request)) {
			log.info("\t > ajax request detected, handling via ExceptionRestHandler");
		} else {
			log.info("\t > non-ajax request detected, handling via ExceptionMvcHandler");
		}
		
		return joinPoint.proceed();
	}
	
	/*
	@Around("resolveExceptionMehtod()")
	public Object resolveWithDynamicResolver(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.resolveWithDynamicResolver", this.getClass().getSimpleName());
		log.info("\t > context = {}", context);
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
		} else {
			log.info("\t > non-ajax request detected, handling via ExceptionMvcHandler");
		}
		
		return joinPoint.proceed();
	}
	*/
}
