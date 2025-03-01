package com.codingjoa.resolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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
	private final Map<String, String> sortOption;
	private final CategoryService categoryService;
	
	public AdminBoardCriResolver(
			@Value("${criteria.board.page}") int defaultPage, 
			@Value("#{${criteria.board.options.recordCnt}}") Map<String, String> recordCntOption, 
			@Value("#{${criteria.board.options.type}}") Map<String, String> typeOption, 
			@Value("#{${criteria.board.options.sort}}") Map<String, String> sortOption, 
			CategoryService categoryService) {
		this.defaultPage = defaultPage;
		this.recordCntOption = recordCntOption;
		this.typeOption = typeOption;
		this.sortOption = sortOption;
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
		String sort = webRequest.getParameter("sort");
		String[] categories = webRequest.getParameterValues("categories");
		log.info("\t > page = {}, recordCnt = {}, keyword = {}, type = {}, sort = {}, categories = {}", 
				page, recordCnt, keyword, type, sort, Arrays.toString(categories));
		
		page = normalize(page);
		recordCnt = normalize(recordCnt);
		keyword = normalize(keyword);
		type = normalize(type);
		sort = normalize(sort);
		
		int defaultRecordCnt = Integer.parseInt(recordCntOption.keySet().iterator().next());
		String defaultType = typeOption.keySet().iterator().next();
		String defaultSort = sortOption.keySet().iterator().next();

		AdminBoardCriteria adminBoardCri = new AdminBoardCriteria(
			NumberUtils.isNaturalNumber(page) ? Integer.parseInt(page) : defaultPage,
			recordCntOption.containsKey(recordCnt) ? Integer.parseInt(recordCnt) : defaultRecordCnt,
			keyword,
			typeOption.containsKey(type) ? type : defaultType,
			sortOption.containsKey(sort) ? sort : defaultSort,
			Collections.emptyList()
		);
		
		log.info("\t > resolved adminBoardCri = {}", adminBoardCri);
		
		Map<String, String> categoryOption = categoryService.getBoardCategoryList()
			.stream()
			.collect(Collectors.toMap(
					category -> category.getCategoryCode().toString(), 
					category -> category.getCategoryName()
			));
		
		Map<String, Object> options = Map.of(
				"recordCntOption", recordCntOption, 
				"typeOption", typeOption,
				"sortOption", sortOption,
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
