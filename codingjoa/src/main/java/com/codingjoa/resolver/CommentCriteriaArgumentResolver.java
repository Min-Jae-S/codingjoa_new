package com.codingjoa.resolver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

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
		
		String boardIdx = getUriTemplateVariables(webRequest).get("boardIdx");
		String page = webRequest.getParameter("page");
		log.info("Raw boardIdx={}, page={}", boardIdx, boardIdx);
		
		return new CommentCriteria(
			Integer.parseInt(boardIdx),
			StringUtils.isNumeric(page) ? Integer.parseInt(page) : defaultPage, 
			defaultRecordCnt
		);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getUriTemplateVariables(NativeWebRequest webRequest) {
		return (Map<String, String>) webRequest.getAttribute(
				HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
	}
}
