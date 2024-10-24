package com.codingjoa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan("com.codingjoa.aop")
@ComponentScan("com.codingjoa.controller") 
@ComponentScan("com.codingjoa.resolver")
@ComponentScan("com.codingjoa.exception")
@EnableAspectJAutoProxy
@Configuration
public class AopConfig {
	
}
