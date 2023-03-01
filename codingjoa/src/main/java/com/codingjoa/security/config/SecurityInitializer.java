package com.codingjoa.security.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
	// https://stackoverflow.com/questions/22548222/both-isanonymous-and-isauthenticated-are-returning-false(top menu, isAnonymous)
	// https://stackoverflow.com/questions/11999656/both-isanonymous-and-isauthenticated-return-false-on-error-page
	// https://develop-writing.tistory.com/97(DistptcherType)
	@Override
	protected EnumSet<DispatcherType> getSecurityDispatcherTypes() {
		//return EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC, DispatcherType.FORWARD);
		return EnumSet.of(DispatcherType.REQUEST, DispatcherType.INCLUDE);
	}
	
}
