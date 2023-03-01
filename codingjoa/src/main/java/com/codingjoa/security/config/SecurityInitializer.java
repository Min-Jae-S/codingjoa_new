package com.codingjoa.security.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	// https://stackoverflow.com/questions/19941466/spring-security-allows-unauthorized-user-access-to-restricted-url-from-a-forward
//	@Override
//	protected EnumSet<DispatcherType> getSecurityDispatcherTypes() {
//		return EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC, DispatcherType.FORWARD);
//	}

	
}
