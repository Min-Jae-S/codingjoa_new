package com.codingjoa.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.codingjoa.filter.EncodingFilter;
import com.codingjoa.filter.LogFilter;
import com.codingjoa.security.config.SecurityConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		log.info("## getRootConfigClasses");
		return new Class[] { 
			RootConfig.class, 
			SecurityConfig.class,
			EmailConfig.class, 
			RedisConfig.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		log.info("## getServletConfigClasses");
		return new Class[] { ServletConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		log.info("## getServletMappings");
		return new String[] { "/" };
	}
	
	@Override
	protected Filter[] getServletFilters() {
		log.info("## getServletFilters");
		return super.getServletFilters();
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		log.info("## customizeRegistration");
		
		/*
		 * MultipartConfigElement(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)
		 * 		location 		 	: 임시폴더 경로, null로 설정시 tomcat이 설정한 임시폴더로 지정
		 * 		maxFileSize		 	: 업로드 하는 파일의 최대 용량(byte 단위)
		 * 		maxRequestSize	 	: 파일 데이터를 포함한 전체 요청 용량
		 * 		fileSizeThreshold 	: 파일 임계값
		 */
		MultipartConfigElement multipartConfig = 
				new MultipartConfigElement("D:\\Dev\\upload\\temp", 20971520, 41943040, 20971520); // 20MB, 40MB, 20MB
		
		//StandardServletMultipartResolver.cleanupMultipart(94) - Failed to perform cleanup of multipart items
		registration.setMultipartConfig(multipartConfig);
	}
	
	@Override
	protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
		log.info("## createDispatcherServlet");
		DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		dispatcherServlet.setEnableLoggingRequestDetails(true);
		
		return dispatcherServlet;
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.info("## onStartup");
		registerCharacterEncodingFilter(servletContext);
		registerLogFilter(servletContext);
		super.onStartup(servletContext);
	}

	private void registerCharacterEncodingFilter(ServletContext servletContext) {
		log.info("## registerCharacterEncodingFilter");
		CharacterEncodingFilter characterEncodingFilter = new EncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		
		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("CharacterEncodingFilter", characterEncodingFilter);
		encodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}
	
	private void registerLogFilter(ServletContext servletContext) {
		log.info("## registerLogFilter");
		FilterRegistration.Dynamic logFilter = servletContext.addFilter("LogFilter", new LogFilter());
		logFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}
	
 
}
