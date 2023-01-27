package com.codingjoa.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codingjoa.annotation.Cri;
import com.codingjoa.pagination.Criteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/criteria.properties")
public class CriteriaArgumentResolver implements HandlerMethodArgumentResolver {

	private final int defaultPage;
	private final int[] recordCntArr;
	private final String[] typeArr;
	
	public CriteriaArgumentResolver(int defaultPage, int[] recordCntArr, String[] typeArr) {
		this.defaultPage = defaultPage;
		this.recordCntArr = recordCntArr;
		this.typeArr = typeArr;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Criteria.class) &&
				parameter.hasParameterAnnotation(Cri.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("============== CriteriaArgumentResolver ==============");
		
		//HttpServletRequest request = (HttpServletRequest) webRequest;
		String page = webRequest.getParameter("page");
		String recordCnt = webRequest.getParameter("recordCnt");
		String type = webRequest.getParameter("type");
		String keyword = webRequest.getParameter("keyword");
		
		return new Criteria(
			StringUtils.isNumeric(page) ? Integer.parseInt(page) : defaultPage,
			isRecordCnt(recordCnt) ? Integer.parseInt(recordCnt) : recordCntArr[0],
			isType(type) ? type : typeArr[0], 
			StringUtils.trim(keyword)
		);
	}
	
	// Arrays.asList(array).contains(value)
	
	private boolean isRecordCnt(String recordCnt) {
		if (!StringUtils.isNumeric(recordCnt)) {
			return false;
		}
		
		for (int i : recordCntArr) {
			if (i == Integer.parseInt(recordCnt)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isType(String type) {
		for (String s : typeArr) {
			if (s.equals(type)) {
				return true;
			}
		}
		
		return false;
	}

}
