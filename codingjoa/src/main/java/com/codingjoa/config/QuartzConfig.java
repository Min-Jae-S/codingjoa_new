package com.codingjoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/WEB-INF/properties/quartz.properties")
public class QuartzConfig {

}
