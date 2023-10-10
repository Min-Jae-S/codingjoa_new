package com.codingjoa.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
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

import com.codingjoa.filter.LogFilter;
import com.codingjoa.security.config.SecurityConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		log.info("## rootConfigClasses");
		Class<?>[] rootConfigClasses = new Class[] { 
				RootConfig.class, 
				SecurityConfig.class,
				EmailConfig.class, 
				RedisConfig.class,
				QuartzConfig.class,
//				BatchConfig.class,
//				BatchJobConfig.class
				BatchConfig2.class,
				BatchJobConfig2.class
		};
		for (Class<?> rootConfigClass : rootConfigClasses) {
			log.info("\t > {}", rootConfigClass);
		}
		return rootConfigClasses;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		log.info("## servletConfigClasses");
		Class<?>[] servletConfigClasses = new Class[] { ServletConfig.class };
		for (Class<?> servletConfigClass : servletConfigClasses) {
			log.info("\t > {}", servletConfigClass);
		}
		return servletConfigClasses;
	}

	@Override
	protected String[] getServletMappings() {
		log.info("## servletMappings");
		String[] servletMappings = new String[] { "/" };
		for (String servletMapping : servletMappings) {
			log.info("\t > \"{}\"", servletMapping);
		}
		return servletMappings;
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
		super.onStartup(servletContext);
		registerCharacterEncodingFilter(servletContext);
		//registerLogFilter(servletContext);
	}

	private void registerCharacterEncodingFilter(ServletContext servletContext) {
		log.info("## registerCharacterEncodingFilter");
		FilterRegistration.Dynamic encodingFilterReg = 
				servletContext.addFilter("CharacterEncodingFilter", new CharacterEncodingFilter());
		encodingFilterReg.setInitParameter("encoding", "UTF-8");
		encodingFilterReg.setInitParameter("forceEncoding", "true");

		// isMatchAfter가 true면 filter의 순서를 뒤에, false면 순서를 앞으로 배치한다.
		EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
		encodingFilterReg.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
	}
	
	@SuppressWarnings("unused")
	private void registerLogFilter(ServletContext servletContext) {
		log.info("## registerLogFilter");
		FilterRegistration.Dynamic logFilterReg = servletContext.addFilter("LogFilter", new LogFilter());
		logFilterReg.setInitParameter("excludePatterns", "/resources/, /upload/");
		
		EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
		logFilterReg.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
	}
}
