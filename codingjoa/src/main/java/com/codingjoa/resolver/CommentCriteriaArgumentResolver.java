package com.codingjoa.resolver;

import java.util.Map;

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
import com.codingjoa.util.MyNumberUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/criteria.properties")
@Component
public class CommentCriteriaArgumentResolver implements HandlerMethodArgumentResolver {

	@Value("${criteria.page}") 
	private int DEFAULT_PAGE;
	
	@Value("${criteria.recordCnt}") 
	private int DEFAULT_RECORD_CNT;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(CommentCriteria.class) &&
				parameter.hasParameterAnnotation(CommentCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("======== CommentCriteriaArgumentResolver ========");
		
		String boardIdx = getUriTemplateVariables(webRequest).get("boardIdx");
		String page = webRequest.getParameter("page");
		log.info("Raw boardIdx = {}, page = {}", boardIdx, page);
		
		return new CommentCriteria(
			Integer.parseInt(boardIdx),
			MyNumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : DEFAULT_PAGE,
			DEFAULT_RECORD_CNT
		);
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getUriTemplateVariables(NativeWebRequest webRequest) { // @PathVariable
		return (Map<String, String>) webRequest.getAttribute(
				HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
	}
}
