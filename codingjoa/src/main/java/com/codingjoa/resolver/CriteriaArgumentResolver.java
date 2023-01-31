package com.codingjoa.resolver;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.Cri;
import com.codingjoa.pagination.Criteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/criteria.properties")
@Component
public class CriteriaArgumentResolver implements HandlerMethodArgumentResolver {
	
	@Value("${criteria.page}") 
	private int defaultPage;
	
	@Value("${criteria.recordCnt}") 
	private int defaultRecordCnt;
	
	@Value("${criteria.type}") 
	private String defaultType;
	
	@Value("#{${criteria.recordCntMap}}") 
	private Map<String, Object> recordCntMap; 
	
	@Value("#{${criteria.typeMap}}") 
	private Map<String, Object> typeMap;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Criteria.class) &&
				parameter.hasParameterAnnotation(Cri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("============== CriteriaArgumentResolver ==============");
		log.info("recordCntMap={}, typeMap={}", recordCntMap, typeMap);
		
		mavContainer.addAttribute("recordCntMap", recordCntMap);
		mavContainer.addAttribute("typeMap", typeMap);
		
		//HttpServletRequest request = (HttpServletRequest) webRequest;
		String boardCategoryCode = webRequest.getParameter("boardCategoryCode");
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		
		return new Criteria(
			Integer.parseInt(boardCategoryCode),
			StringUtils.isNumeric(page) ? Integer.parseInt(page) : defaultPage,
			recordCntMap.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : defaultRecordCnt,
			typeMap.containsKey(type) ? type : defaultType,
			StringUtils.trim(keyword)
		);
	}
	

}
