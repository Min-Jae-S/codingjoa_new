package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.codingjoa.annotation.CommentCri;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.util.MyUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class CommentCriteriaArgumentResolver implements HandlerMethodArgumentResolver {

	private int page;
	private int recordCnt;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(CommentCriteria.class) &&
				parameter.hasParameterAnnotation(CommentCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		
		String rawPage = webRequest.getParameter("page");
		log.info("\t > rawPage = {};", rawPage);
		
		rawPage = (rawPage == null) ? "" : rawPage.strip();
		return new CommentCriteria(
			MyUtils.isPageNumber(rawPage) ? Integer.parseInt(rawPage) : page,
			recordCnt
		);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private Map<String, String> getUriTemplateVariables(NativeWebRequest webRequest) { // @PathVariable
		return (Map<String, String>) webRequest.getAttribute(
				HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
	}
}
