package com.codingjoa.resolver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.CommentCri;
import com.codingjoa.pagination.CommentCriteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/criteria.properties")
@Component
public class CommentCriteriaArgumentResolver implements HandlerMethodArgumentResolver {

	@Value("${criteria.page}") 
	private int defaultPage;
	
	@Value("${criteria.recordCnt}") 
	private int defaultRecordCnt;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(CommentCriteria.class) &&
				parameter.hasParameterAnnotation(CommentCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("============== CommentCriteriaArgumentResolver ==============");
		
		String boardIdx = webRequest.getParameter("boardIdx");
		String page = webRequest.getParameter("page");
		
		return null;
	}


}
