package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.BoardCri;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.util.MyNumberUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/criteria.properties")
@Component
public class BoardCriteriaArgumentResolver implements HandlerMethodArgumentResolver {
	
	@Value("${criteria.page}") 
	private int DEFAULT_PAGE;
	
	@Value("${criteria.recordCnt}") 
	private int DEFAULT_RECORD_CNT;
	
	@Value("${criteria.type}") 
	private String DEFALUT_TYPE;
	
	@Value("#{${criteria.recordCntMap}}") 
	private Map<String, Object> recordCntMap; 
	
	@Value("#{${criteria.typeMap}}") 
	private Map<String, Object> typeMap;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(BoardCriteria.class) &&
				parameter.hasParameterAnnotation(BoardCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("============== BoardCriteriaArgumentResolver ==============");
		
		mavContainer.addAttribute("recordCntMap", recordCntMap);
		mavContainer.addAttribute("typeMap", typeMap);
		
		int boardCategoryCode;
		try {
			String rawBoardCategoryCode = webRequest.getParameter("boardCategoryCode");
			boardCategoryCode = Integer.parseInt(rawBoardCategoryCode);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Not valid boardCategoryCode");
		}
		
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		
		return new BoardCriteria(
			boardCategoryCode,
			MyNumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : DEFAULT_PAGE,
			recordCntMap.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : DEFAULT_RECORD_CNT,
			typeMap.containsKey(type) ? type : DEFALUT_TYPE,
			keyword == null ? null : keyword.trim()
		);
	}
	

}
