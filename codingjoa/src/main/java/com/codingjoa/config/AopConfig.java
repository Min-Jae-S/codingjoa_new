package com.codingjoa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan("com.codingjoa.aop")
@EnableAspectJAutoProxy
@Configuration
public class AopConfig {

}
