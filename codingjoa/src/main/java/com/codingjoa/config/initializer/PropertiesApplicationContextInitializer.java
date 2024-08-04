package com.codingjoa.config.initializer;

import java.io.IOException;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;

import lombok.extern.slf4j.Slf4j;

/*
 * The ApplicationContext processes properties during its initialization. 
 * If property processing is needed before the ApplicationContext initialization (refresh), use ApplicationContextInitializer."
 * 
 */

@Slf4j
public class PropertiesApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> { // EnviromentPostProcessor
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		log.info("## {}.initialize", this.getClass().getSimpleName());
		ConfigurableEnvironment env = applicationContext.getEnvironment();
		
		try {
			Resource[] resources = applicationContext.getResources("WEB-INF/properties/*.properties");
			for (Resource resource : resources) {
				if (resource.exists()) {
					PropertySource<?> propertySource = new ResourcePropertySource(resource);
					env.getPropertySources().addLast(propertySource);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

