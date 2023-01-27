package com.codingjoa.resolver;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.Cri;
import com.codingjoa.pagination.Criteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CriteriaArgumentResolver implements HandlerMethodArgumentResolver {

	private final int defaultPage;
	private final Map<String, String> recordCntMap;
	private final Map<String, String> typeMap;
	
	public CriteriaArgumentResolver(int defaultPage, 
			Map<String, String> recordCntMap, Map<String, String> typeMap) {
		this.defaultPage = defaultPage;
		this.recordCntMap = recordCntMap;
		this.typeMap = typeMap;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Criteria.class) &&
				parameter.hasParameterAnnotation(Cri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("============== CriteriaArgumentResolver ==============");
		
		//HttpServletRequest request = (HttpServletRequest) webRequest;
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		
		return new Criteria(
			StringUtils.isNumeric(page) ? Integer.parseInt(page) : defaultPage,
			recordCntMap.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : 10,
			typeMap.containsKey(type) ? type : "T",
			StringUtils.trim(keyword)
		);
	}
	

}
