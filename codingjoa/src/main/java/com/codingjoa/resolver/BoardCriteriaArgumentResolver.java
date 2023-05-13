package com.codingjoa.resolver;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.BoardCri;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.util.MyNumberUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class BoardCriteriaArgumentResolver implements HandlerMethodArgumentResolver {
	
	private int page;
	private int recordCnt;
	private String type;
	private Map<String, Object> recordCntMap; 
	private Map<String, Object> typeMap;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Criteria.class) &&
				parameter.hasParameterAnnotation(BoardCri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		log.info("	default page = {}", page);
		log.info("	default recordCnt = {}", recordCnt);
		log.info("	default type = {}", type);
		log.info("	recordCntMap = {}", recordCntMap);
		log.info("	typeMap = {}", typeMap);
		
		mavContainer.addAttribute("recordCntMap", recordCntMap);
		mavContainer.addAttribute("typeMap", typeMap);
		
		String rawPage = webRequest.getParameter("page");
		String rawRecordCnt = webRequest.getParameter("recordCnt");
		String rawType = webRequest.getParameter("type");
		String rawKeyword = webRequest.getParameter("keyword");
		log.info("	raw page = {}", rawPage);
		log.info("	raw recordCnt = {}", rawRecordCnt);
		log.info("	raw type = {}", rawType);
		log.info("	raw keyword = {}", rawKeyword);
		
		return new Criteria(
			MyNumberUtils.isNaturalNumber(rawPage) ? Integer.parseInt(rawPage) : page,
			recordCntMap.containsKey(rawRecordCnt) ? Integer.parseInt(rawRecordCnt) : recordCnt,
			typeMap.containsKey(rawType) ? rawType : type,
			rawKeyword == null ? null : rawKeyword.strip()
		);
	}
}
