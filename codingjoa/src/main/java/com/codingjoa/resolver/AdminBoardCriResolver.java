package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.AdminBoardCri;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.util.NumberUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminBoardCriResolver implements HandlerMethodArgumentResolver {
	
	private final int defaultPage;
	private final Map<String, String> recordCntOption;
	private final Map<String, String> typeOption;
	
	public AdminBoardCriResolver(
			@Value("${criteria.board.page}") int defaultPage, 
			@Value("#{${criteria.board.options.recordCnt}}") Map<String, String> recordCntOption, 
			@Value("#{${criteria.board.options.type}}") Map<String, String> typeOption) {
		this.defaultPage = defaultPage;
		this.recordCntOption = recordCntOption;
		this.typeOption = typeOption;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(AdminBoardCriteria.class) && 
				parameter.hasParameterAnnotation(AdminBoardCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		log.info("\t > page = {}, recordCnt = {}, type = {}, keyword = {}", page, recordCnt, type, keyword);
		
		page = normalize(page);
		recordCnt = normalize(recordCnt);
		type = normalize(type);
		keyword = normalize(keyword);
		
		int defaultRecordCnt = Integer.parseInt(recordCntOption.keySet().iterator().next());
		String defaultType = typeOption.keySet().iterator().next();

		AdminBoardCriteria adminBoardCri = new AdminBoardCriteria(
			NumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : defaultPage,
			recordCntOption.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : defaultRecordCnt,
			typeOption.containsKey(type) ? type : defaultType,
			keyword
		);
		
		log.info("\t > resolved adminBoardCri = {}", adminBoardCri);

		return adminBoardCri;
	}
	
	private String normalize(String value) {
		return (value == null) ? "" : value.trim();
	}
}
