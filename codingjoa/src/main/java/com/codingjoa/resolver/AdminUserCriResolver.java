package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.AdminUserCri;
import com.codingjoa.pagination.AdminUserCriteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminUserCriResolver implements HandlerMethodArgumentResolver {
	
	private final int defaultPage;
	private final int defaultRecordCnt;
	
	public AdminUserCriResolver(
			@Value("${criteria.user.page}") int defaultPage, 
			@Value("${criteria.user.recordCnt}") int defaultRecordCnt) {
		this.defaultPage = defaultPage;
		this.defaultRecordCnt = defaultRecordCnt;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(AdminUserCriteria.class) &&
				parameter.hasParameterAnnotation(AdminUserCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		
		AdminUserCriteria adminUserCri = new AdminUserCriteria(defaultPage, defaultRecordCnt);
		log.info("\t > resolved adminUserCri = {}", adminUserCri);
		
		return adminUserCri;
	}
	
	public Map<String, Object> getOptions() {
		return null;
	}
	
}
