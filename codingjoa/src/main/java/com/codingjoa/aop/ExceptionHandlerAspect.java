package com.codingjoa.aop;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class ExceptionHandlerAspect {
	
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
	@Around("excpetionResolutionInComposite()")								// HandlerExceptionResolverComposite.resolveException(..)
	public Object arroundResolution(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("## {}.arroundResolution", this.getClass().getSimpleName());
		log.info("\t > target = {}", joinPoint.getTarget().getClass().getSimpleName());
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		List<String> parameterTypes = Arrays.stream(signature.getParameterTypes())
				.map(parameterType -> parameterType.getSimpleName())
				.collect(Collectors.toList());
		log.info("\t > parameterTypes = {}", parameterTypes);
		
		HttpServletRequest request = null;
		for (Object arg : joinPoint.getArgs()) {
			// HttpServletRequest, HttpServletResponse, Object(handler), Exception
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				break;
			}
		}
		
		if (request == null) {
			log.info("\t > no HttpServletRequest instance in arguments");
		} 
		
		return joinPoint.proceed();
	}
}
