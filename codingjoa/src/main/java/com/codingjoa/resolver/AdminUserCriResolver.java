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
import com.codingjoa.util.NumberUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminUserCriResolver implements HandlerMethodArgumentResolver {
	
	private final int defaultPage;
	private final Map<String, String> recordCntOption;
	private final Map<String, String> typeOption;
	
	public AdminUserCriResolver(
			@Value("${criteria.user.page}") int defaultPage, 
			@Value("#{${criteria.user.options.recordCnt}}") Map<String, String> recordCntOption, 
			@Value("#{${criteria.user.options.type}}") Map<String, String> typeOption) {
		this.defaultPage = defaultPage;
		this.recordCntOption = recordCntOption;
		this.typeOption = typeOption;
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
		
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		log.info("\t > page = {}, recordCnt = {}, keyword = {}, type = {}", page, recordCnt, keyword, type);
		
		int defaultRecordCnt = Integer.parseInt(recordCntOption.keySet().iterator().next());
		String defaultType = typeOption.keySet().iterator().next();
		
		AdminUserCriteria adminUserCri = new AdminUserCriteria(
			NumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : defaultPage,
			recordCntOption.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : defaultRecordCnt, 
			keyword == null ? "" : keyword.trim(),
			typeOption.containsKey(type) ? type : defaultType
		);
			
		log.info("\t > resolved adminUserCri = {}", adminUserCri);

		return adminUserCri;
	}
	
	public Map<String, Object> getOptions() {
		return Map.of("recordCntOption", recordCntOption, "typeOption", typeOption);
	}
	
}
