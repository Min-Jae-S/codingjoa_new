package com.codingjoa.resolver;

import java.util.Collections;
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
import com.codingjoa.service.CategoryService;
import com.codingjoa.util.NumberUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminBoardCriResolver implements HandlerMethodArgumentResolver {
	
	private final int defaultPage;
	private final Map<String, String> recordCntOption;
	private final Map<String, String> typeOption;
	private final CategoryService categoryService;
	
	public AdminBoardCriResolver(
			@Value("${criteria.board.page}") int defaultPage, 
			@Value("#{${criteria.board.options.recordCnt}}") Map<String, String> recordCntOption, 
			@Value("#{${criteria.board.options.type}}") Map<String, String> typeOption,
			CategoryService categoryService) {
		this.defaultPage = defaultPage;
		this.recordCntOption = recordCntOption;
		this.typeOption = typeOption;
		this.categoryService = categoryService;
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
		log.info("\t > categories = {}", webRequest.getParameter("categories"));
		
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
			keyword,
			Collections.emptyList()
		);
		
		log.info("\t > resolved adminBoardCri = {}", adminBoardCri);
		
		Map<String, String> categoryOption = null;
//		Map<String, String> categoryOption = categoryService.getBoardCategoryList()
//			.stream()
//			.collect(Collectors.toMap(
//					category -> category.getCategoryCode().toString(), 
//					category -> category.getCategoryName()
//			));
		
		Map<String, Object> options = Map.of(
				"recordCntOption", recordCntOption, 
				"typeOption", typeOption,
				"categoryOption", categoryOption
			);
		
		mavContainer.addAttribute("options", options);
		log.info("\t > add attr to mavContainer: options {}", options.keySet());

		return adminBoardCri;
	}
	
	private String normalize(String value) {
		return (value == null) ? "" : value.trim();
	}
}
