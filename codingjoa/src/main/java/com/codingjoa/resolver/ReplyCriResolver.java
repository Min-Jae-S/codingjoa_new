package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.codingjoa.annotation.ReplyCri;
import com.codingjoa.pagination.ReplyCriteria;
import com.codingjoa.util.NumberUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReplyCriResolver implements HandlerMethodArgumentResolver {

	private final int defaultPage;
	private final int defaultRecordCnt;
	
	public ReplyCriResolver(
			@Value("${criteria.reply.page}") int defaultPage, 
			@Value("${criteria.reply.recordCnt}") int defaultRecordCnt) {
		this.defaultPage = defaultPage;
		this.defaultRecordCnt = defaultRecordCnt;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(ReplyCriteria.class) &&
				parameter.hasParameterAnnotation(ReplyCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		
		String page = webRequest.getParameter("page");
		log.info("\t > page = {}", page);
		
		page = (page == null) ? "" : page.trim();
		ReplyCriteria replyCri = new ReplyCriteria(
			NumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : defaultPage,
			defaultRecordCnt
		);
		
		log.info("\t > resolved replyCri = {}", replyCri);
		
		return replyCri;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private Map<String, String> getUriTemplateVariables(NativeWebRequest webRequest) { // @PathVariable
		return (Map<String, String>) webRequest.getAttribute(
				HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
	}

}
