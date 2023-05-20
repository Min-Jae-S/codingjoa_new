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
		log.info("-------- getRootConfigClasses -------");
		return new Class[] { 
			RootConfig.class, 
			SecurityConfig.class,
			EmailConfig.class, 
			RedisConfig.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		log.info("-------- getServletConfigClasses -------");
		return new Class[] { ServletConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		log.info("-------- getServletMappings -------");
		return new String[] { "/" };
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		log.info("-------- customizeRegistration -------");
		
		// new MultipartConfigElement(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)
		// location 			: 임시폴더 경로, null로 설정시 tomcat이 설정한 임시폴더로 지정
		// maxFileSize			: 업로드 하는 파일의 최대 용량(byte 단위)
		// maxRequestSize		: 파일 데이터를 포함한 전체 요청 용량
		// fileSizeThreshold	: 파일 임계값
		MultipartConfigElement multipartConfig = 
				new MultipartConfigElement("D:\\Dev\\upload\\temp", 20971520, 41943040, 20971520); // 20MB, 40MB, 20MB
		
		//StandardServletMultipartResolver.cleanupMultipart(94) - Failed to perform cleanup of multipart items
		registration.setMultipartConfig(multipartConfig);
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.info("-------- onStartup -------");
		super.onStartup(servletContext);
		
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		
		FilterRegistration registration1 = servletContext.addFilter("CharacterEncodingFilter", encodingFilter);
		registration1.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
		
		FilterRegistration registration2 = servletContext.addFilter("LogFilter", new LogFilter());
		registration2.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}

	@Override
	protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
		log.info("-------- createDispatcherServlet -------");
		DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		
//		Enumeration<String> initParameterNames = dispatcherServlet.getInitParameterNames();
//		while (initParameterNames.hasMoreElements()) {
//			log.info("\t > {}", initParameterNames.nextElement());
//		}
		
		return dispatcherServlet;
	}

}
