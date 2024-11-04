package com.codingjoa.aop;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.codingjoa.util.AjaxUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class ExceptionHandlerAspect {
	
	//private final ApplicationContext context;
	private final WebApplicationContext context;
	
	@Pointcut("execution(* com.codingjoa.exception.*.*(..))")
	public void inExceptionHandlerPackage() {}
	
	@Pointcut("@within(org.springframework.web.bind.annotation.ControllerAdvice) || "
			+ "@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
	public void withinControllerAdviceAnnotations() {}
	
	@Pointcut("execution(* org.springframework.web.servlet.handler.HandlerExceptionResolverComposite.resolveException(..))")
	public void excpetionResolutionInComposite() {}
	
	@Pointcut("execution(* org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver.resolveException(..))")
	public void excpetionResolutionInExceptionHandlerExceptionResolver() {}

	// filter for selecting @ExceptionHandler methods 
	@Pointcut("execution(* org.springframework.web.method.annotation.ExceptionHandlerMethodResolver.resolveMethod(..))")
	public void methodResolutionInMethodResolver() {}
	
	/*
	public class HandlerExceptionResolverComposite implements HandlerExceptionResolver, Ordered {
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
	
	public class ExceptionHandlerExceptionResolver extends AbstractHandlerMethodExceptionResolver
		implements ApplicationContextAware, InitializingBean {
		
		@Override
		@Nullable
		protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
				HttpServletResponse response, @Nullable HandlerMethod handlerMethod, Exception exception) {

			ServletInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);
			if (exceptionHandlerMethod == null) {
				return null;
			}

			...
		}
	}
	*/
	
	//@Around("methodResolutionInMethodResolver()") 						// ExceptionHandlerMethodResolver.resolveMethod(..)
	//@Around("excpetionResolutionInExceptionHandlerExceptionResolver()") 	// ExceptionHandlerExceptionResolver.resolveException(..)
	//@Around("excpetionResolutionInComposite()")							// HandlerExceptionResolverComposite.resolveException(..)
	@Around("execution(* org.springframework.web.servlet.handler.HandlerExceptionResolverComposite.resolveException(..))")
	public Object arroundResolution(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.arroundResolution", this.getClass().getSimpleName());
		log.info("\t > target = {}", joinPoint.getTarget().getClass().getSimpleName());
		
		HttpServletRequest request = null;
		Object[] args = joinPoint.getArgs(); // HttpServletRequest, HttpServletResponse, HandlerMethod, Exception
		
		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			log.info("\t > arg[{}] = {}", i, obj);
			if (args[i] instanceof HttpServletRequest) {
				request = (HttpServletRequest) args[i];
			}
		}
		
		if (request == null) {
			log.info("\t > no HttpServletRequest instance in arguments");
		}
		
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
