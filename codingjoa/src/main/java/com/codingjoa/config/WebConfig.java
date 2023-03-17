package com.codingjoa.config;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.codingjoa.security.config.SecurityConfig;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfig.class, SecurityConfig.class, EmailConfig.class, RedisConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { ServletConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);

		return new Filter[] { encodingFilter };
	}

	@Override
	protected void customizeRegistration(Dynamic registration) {
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
	
	
}
