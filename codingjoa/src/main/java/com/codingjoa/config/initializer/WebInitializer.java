package com.codingjoa.config.initializer;

import java.io.File;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.codingjoa.config.AppConfig;
import com.codingjoa.config.BatchConfig;
import com.codingjoa.config.BatchJobConfig;
import com.codingjoa.config.DataSourceConfig;
import com.codingjoa.config.EmailConfig;
import com.codingjoa.config.ModelMapperConfig;
import com.codingjoa.config.MybatisConfig;
import com.codingjoa.config.OAuth2Config;
import com.codingjoa.config.QuartzConfig;
import com.codingjoa.config.RedisConfig;
import com.codingjoa.config.SecurityConfig;
import com.codingjoa.config.ServletConfig;
import com.codingjoa.filter.LogFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		log.info("## getRootConfigClasses");
		return new Class[] {
				AppConfig.class,
				DataSourceConfig.class,
				MybatisConfig.class,
				SecurityConfig.class,
				ModelMapperConfig.class,
				EmailConfig.class, 
				RedisConfig.class,
				QuartzConfig.class,
				BatchConfig.class,
				BatchJobConfig.class,
				OAuth2Config.class
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
	protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
		log.info("## getRootApplicationContextInitializers");
		//ApplicationContextInitializer<?>[] existingInitializers = super.getRootApplicationContextInitializers(); // null
		return new ApplicationContextInitializer<?>[] { new PropertiesApplicationContextInitializer() };
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
	
	/*
	 * @@ MultipartConfigElement(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)
	 * 		location 		 	: 임시폴더 경로, null로 설정시 tomcat이 설정한 임시폴더로 지정
	 * 		maxFileSize		 	: 업로드 하는 파일의 최대 용량(byte 단위)
	 * 		maxRequestSize	 	: 파일 데이터를 포함한 전체 요청 용량
	 * 		fileSizeThreshold 	: 파일 임계값
	 */

	@Override
	protected void customizeRegistration(Dynamic registration) {
		log.info("## customizeRegistration");
		
		String tempDir = "D:\\Dev\\upload\\temp";
		File tmpFolder = new File(tempDir);
		if (!tmpFolder.exists()) {
			if (!tmpFolder.mkdirs()) {
				String defaultTempDir = System.getProperty("java.io.tmpdir");
				tempDir = defaultTempDir;
				log.warn("failed to create directory: " + tempDir + ". using default temp directory: " + defaultTempDir);
			}
		}
		
		int maxFileSize = (int) DataSize.ofMegabytes(20).toBytes();
		int maxRequestSize = (int) DataSize.ofMegabytes(40).toBytes();
		int fileSizeThreshold = (int) DataSize.ofMegabytes(20).toBytes();
		
		MultipartConfigElement config = new MultipartConfigElement(tempDir, maxFileSize, maxRequestSize, fileSizeThreshold);
		
		//StandardServletMultipartResolver.cleanupMultipart(94) - Failed to perform cleanup of multipart items
		registration.setMultipartConfig(config);
	}
	
	private void registerCharacterEncodingFilter(ServletContext servletContext) {
		log.info("## registerCharacterEncodingFilter");
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("CharacterEncodingFilter", characterEncodingFilter);
		filterRegistration.setInitParameter("encoding", "UTF-8");
		filterRegistration.setInitParameter("forceEncoding", "true");

		// If isMatchAfter is set to true, the filter is placed after existing filters in the chain; 
		// if false, the filter is placed before existing filters.
		filterRegistration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}
	
	@SuppressWarnings("unused")
	private void registerLogFilter(ServletContext servletContext) {
		log.info("## registerLogFilter");
		LogFilter logFilter = new LogFilter();
		FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("LogFilter", logFilter);
		filterRegistration.setInitParameter("excludePatterns", "/resources/, /upload/");
		
		EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
		filterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
	}
	
}
