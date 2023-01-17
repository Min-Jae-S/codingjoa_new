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
	private final int DEFAULT_RECORD_CNT = 10;
	
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
		
		return new Criteria(
			!StringUtils.isNumeric(page) ? DEFAULT_PAGE : Integer.parseInt(page), 
			!StringUtils.isNumeric(recordCnt) ? DEFAULT_RECORD_CNT : Integer.parseInt(recordCnt)
		);
	}

}
