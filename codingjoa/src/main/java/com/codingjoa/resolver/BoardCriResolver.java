package com.codingjoa.resolver;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.BoardCri;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.util.NumberUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BoardCriResolver implements HandlerMethodArgumentResolver {
	
	private final int defaultPage;
	private final LinkedHashMap<String, String> recordOption;
	private final LinkedHashMap<String, String> typeOption;
	
	public BoardCriResolver(
			@Value("${criteria.board.page}") int defaultPage, 
			@Value("#{${criteria.board.options.recordCnt}}") Map<String, String> recordCntOption, 
			@Value("#{${criteria.board.options.type}}") Map<String, String> typeOption) {
		this.defaultPage = defaultPage;
		this.recordOption = new LinkedHashMap<>(recordCntOption);
		this.typeOption = new LinkedHashMap<>(typeOption);
		log.info("## recordOption = {}", recordCntOption.getClass());
		log.info("## typeOption = {}", typeOption.getClass());
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(BoardCriteria.class) && 
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
		log.info("\t > page = {}, recordCnt = {}, type = {}, keyword = {}", page, recordCnt, type, keyword);
		
		page = (page == null) ? "" : page.strip();
		recordCnt = (recordCnt == null) ? "" : recordCnt.strip();
		type = (type == null) ? "" : type.strip();
		keyword = (keyword == null) ? "" : keyword.strip();
		
		int defaultRecordCnt = Integer.parseInt(recordOption.keySet().iterator().next());
		String defaultType = typeOption.keySet().iterator().next();
		log.info("\t > defaultRecordCnt = {}, defaultType = {}", defaultRecordCnt, defaultType);

		BoardCriteria boardCri = new BoardCriteria(
			NumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : defaultPage,
			recordOption.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : defaultRecordCnt,
			typeOption.containsKey(type) ? type : defaultType,
			keyword
		);
		
		log.info("\t > resolved boardCri = {}", boardCri);

		Map<String, Map<String, String>> options = Map.of("recordOption", recordOption, 
				"typeOption", typeOption);
		mavContainer.addAttribute("options", options);
		log.info("\t > add model attr = options [{}]", options.keySet());
		
		return boardCri;
	}
	
}
