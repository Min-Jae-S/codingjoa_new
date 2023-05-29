package com.codingjoa.security.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
	// https://stackoverflow.com/questions/22548222/both-isanonymous-and-isauthenticated-are-returning-false(top menu, isAnonymous)
	// https://stackoverflow.com/questions/11999656/both-isanonymous-and-isauthenticated-return-false-on-error-page
	// https://develop-writing.tistory.com/97(DistptcherType)
	
	/*
	 * SecurityFilterChain의 인스턴스에서 호출되는 메서드로서 
	 * 해당 필터 체인이 어떤 Dispatcher Type에 대해 동작해야 하는지를 지정할 수 있다.
	 * 예를 들어 특정 필터 체인이 비동기 요청에만 적용되도록 할 경우, DispatcherType.ASYNC를 반환시킨다.
	 */
	
	/*
	 * Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
	 * 	if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	 * 		// <sec:authorize access="isAnonymous()"></sec:authorize> 
	 * 	} else if(authentication.isAuthenticated()) { 
	 * 		// <sec:authorize access="isAuthenticated()"></sec:authorize> 
	 * 	}
	 */
	
	@Override
	protected EnumSet<DispatcherType> getSecurityDispatcherTypes() {
		log.info("## getSecurityDispatcherTypes");
		
		//EnumSet<DispatcherType> securityDispatcherTypes = super.getSecurityDispatcherTypes(); // REQUEST, ASYNC, ERROR
		//EnumSet<DispatcherType> securityDispatcherTypes = EnumSet.allOf(DispatcherType.class)
		//EnumSet<DispatcherType> securityDispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.INCLUDE);
		EnumSet<DispatcherType> securityDispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
		log.info("\t > {}", securityDispatcherTypes);
		
		return securityDispatcherTypes;
	}

	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
		log.info("## beforeSpringSecurityFilterChain");
		super.beforeSpringSecurityFilterChain(servletContext);
	}

	@Override
	protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
		log.info("## afterSpringSecurityFilterChain");
		super.afterSpringSecurityFilterChain(servletContext);
	}
	
	
}
