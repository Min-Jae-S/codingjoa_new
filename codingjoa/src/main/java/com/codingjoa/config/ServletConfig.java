package com.codingjoa.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codingjoa.interceptor.CheckBoardIdxInterceptor;
import com.codingjoa.interceptor.CheckBoardCategoryInterceptor;
import com.codingjoa.interceptor.CheckMyBoardInterceptor;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;

@Configuration
@EnableWebMvc
@PropertySource("/WEB-INF/properties/upload.properties")
@ComponentScan("com.codingjoa.controller")
public class ServletConfig implements WebMvcConfigurer {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BoardService boardService;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:///" + uploadPath);
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		//StringHttpMessageConverter defaults to ISO-8859-1
		
		WebMvcConfigurer.super.extendMessageConverters(converters);  
		converters.stream()
			.filter(converter -> converter instanceof StringHttpMessageConverter)
			.forEach(converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new BeforeUpdatePasswordInterceptor())
//				.addPathPatterns("/member/updatePassword");
		registry.addInterceptor(new CheckBoardCategoryInterceptor(categoryService))
				.addPathPatterns("/board/main", "/board/write", "/board/writeProc", "/board/modifyProc");
		registry.addInterceptor(new CheckBoardIdxInterceptor(boardService))
				.addPathPatterns("/board/read");
		registry.addInterceptor(new CheckMyBoardInterceptor(boardService))
				.order(-1)
				.addPathPatterns("/board/modify", "/board/modifyProc");
		
	}
	
	// MultipartResolver : StandardServletMultipartResolver, CommonsMultipartResolver
	// CommonsMultipartResolver ????????? commons-fileupload ??????????????? ??????
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
}
