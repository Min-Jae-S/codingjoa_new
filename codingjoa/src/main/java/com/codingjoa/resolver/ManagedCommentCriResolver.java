package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.ManagedCommentCri;
import com.codingjoa.pagination.ManagedCommentCriteria;
import com.codingjoa.util.NumberUtils;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Component
public class ManagedCommentCriResolver implements HandlerMethodArgumentResolver {

	private final int defaultPage;
	private final int defaultRecordCnt;
	private final String defaultType;
	private final Map<String, Object> recordCntGroup;
	private final Map<String, Object> typeGroup;
	
	public ManagedCommentCriResolver(
			@Value("${criteria.comment.page}") int defaultPage, 
			@Value("${criteria.comment.recordCnt}") int defaultRecordCnt, 
			@Value("${criteria.comment.type}") String defaultType,
			@Value("#{${criteria.comment.recordCntGroup}}") Map<String, Object> recordCntGroup, 
			@Value("#{${criteria.comment.typeGroup}}") Map<String, Object> typeGroup) {
		this.defaultPage = defaultPage;
		this.defaultRecordCnt = defaultRecordCnt;
		this.defaultType = defaultType;
		this.recordCntGroup = recordCntGroup;
		this.typeGroup = typeGroup;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(ManagedCommentCriResolver.class) &&
				parameter.hasParameterAnnotation(ManagedCommentCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		
		String page = webRequest.getParameter("page");
		String recordCnt =  webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		log.info("\t > page = {}, recordCnt = {}, type = {}, keyword = {}", page, recordCnt, type, keyword);
		
		page = (page == null) ? "" : page.strip();
		recordCnt = (recordCnt == null) ? "" : recordCnt.strip();
		type = (type == null) ? "" : type.strip();
		keyword = (keyword == null) ? "" : keyword.strip();
		
		ManagedCommentCriteria adminCommentCri = new ManagedCommentCriteria(
			NumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : defaultPage,
			defaultRecordCnt
		);
		
		log.info("\t > resolved adminCommentCri = {}", adminCommentCri);
		
		return adminCommentCri;
	}
}
