package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.BoardCri;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.util.MyUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class BoardCriteriaArgumentResolver implements HandlerMethodArgumentResolver {
	
	private int defaultPage;
	private int defaultRecordCnt;
	private String defaultType;
	private Map<String, Object> recordCntGroup; 
	private Map<String, Object> typeGroup;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Criteria.class) && 
				parameter.hasParameterAnnotation(BoardCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		log.info("\t > page = {}, recordCnt = {}, type = {}, keyword = {}", 
				page, recordCnt, type, (keyword == null) ? keyword : "'" + keyword + "'");
		
		page = (page == null) ? "" : page.strip();
		recordCnt = (recordCnt == null) ? "" : recordCnt.strip();
		type = (type == null) ? "" : type.strip();
		keyword = (keyword == null) ? "" : keyword.strip();

		Criteria boardCri = new Criteria(
			MyUtils.isPageNumber(page) ? Integer.parseInt(page) : defaultPage,
			recordCntGroup.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : defaultRecordCnt,
			typeGroup.containsKey(type) ? type : defaultType,
			keyword
		);
		log.info("\t > resolve boardCri = {}", boardCri);
		
		mavContainer.addAttribute("boardCri", boardCri);
		mavContainer.addAttribute("recordCntGroup", recordCntGroup);
		mavContainer.addAttribute("typeGroup", typeGroup);
		
		return boardCri;
	}
}
