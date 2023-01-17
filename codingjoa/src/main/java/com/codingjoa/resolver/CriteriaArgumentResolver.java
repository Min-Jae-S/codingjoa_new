package com.codingjoa.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.pagination.Criteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CriteriaArgumentResolver implements HandlerMethodArgumentResolver {

	private final int DEFAULT_PAGE = 1;
	private final String[] DEFAULT_RECORD_CNT_ARR = { "10", "20", "30" };
	private final String[] DEFAULT_TYPE_ARR = { "T", "C", "W", "TW" };
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Criteria.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("============== CriteriaArgumentResolver ==============");
		
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String keyword = webRequest.getParameter("keyword");
		String type = webRequest.getParameter("type");
		
		return new Criteria(
			StringUtils.isNumeric(page) ? Integer.parseInt(page) : DEFAULT_PAGE, 
			isRecordCnt(recordCnt) ? Integer.parseInt(recordCnt) : Integer.parseInt(DEFAULT_RECORD_CNT_ARR[0]),
			StringUtils.trim(keyword),
			isType(type) ? type : DEFAULT_TYPE_ARR[0] 
		);
	}
	
	
	private boolean isRecordCnt(String recordCnt) {
		for (String s : DEFAULT_RECORD_CNT_ARR) {
			if (s.equals(recordCnt)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isType(String type) {
		for (String s : DEFAULT_TYPE_ARR) {
			if (s.equals(type)) {
				return true;
			}
		}
		
		return false;
	}

}
