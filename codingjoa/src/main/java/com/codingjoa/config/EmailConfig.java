package com.codingjoa.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@PropertySource("/WEB-INF/properties/mail.properties")
public class EmailConfig {
	
	@Autowired
	private Environment env;

	@Bean
	public JavaMailSender mailSeneder() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("mail.host"));
		mailSender.setPort(env.getProperty("mail.port", Integer.class));
		mailSender.setUsername(env.getProperty("mail.username"));
		mailSender.setPassword(env.getProperty("mail.password"));
		mailSender.setJavaMailProperties(javaMailProperties());
		
		return mailSender;
	}
	
	private Properties javaMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", env.getProperty("mail.auth"));
		properties.put("mail.smtp.starttls.enable", env.getProperty("mail.enable"));
		
		return properties;
	}
	
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine templaEngine = new SpringTemplateEngine();
		templaEngine.addTemplateResolver(springResourceTemplateResolver());
		
		return templaEngine;
	}
	
	@Bean
	public SpringResourceTemplateResolver springResourceTemplateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false);
		
		return resolver;
	}
}
