package com.codingjoa.config;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		//registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.info("-------- onStartup -------");

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter() {

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				log.info("-------- {} doFilterInternal --------", this.getClass().getSimpleName());
				super.doFilterInternal(request, response, filterChain);
			}

			@Override
			protected void initFilterBean() throws ServletException {
				log.info("-------- {} init --------", this.getFilterConfig().getFilterName());
				super.initFilterBean();
			}
			
		};
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		
		FilterRegistration registration1 = servletContext.addFilter("CharacterEncodingFilter", encodingFilter);
		registration1.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
		
		LogFilter logFilter = new LogFilter();
		FilterRegistration registration2 = servletContext.addFilter("LogFilter", logFilter);
		registration2.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false, "/*");
		
		super.onStartup(servletContext);
	}

	@Override
	protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
		log.info("-------- createDispatcherServlet -------");
		DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		
		return dispatcherServlet;
	}
	
}
