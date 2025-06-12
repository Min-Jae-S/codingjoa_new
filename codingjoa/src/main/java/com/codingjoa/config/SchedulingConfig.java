package com.codingjoa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("com.codingjoa.scheduler")
@EnableScheduling
@Configuration
public class SchedulingConfig {

}
