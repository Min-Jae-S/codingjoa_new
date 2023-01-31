package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

// "/board/main"											--> Criteria(boardCategoryCode)
// "/board/write", "/board/writeProc", "/board/modifyProc"	--> BoardDto(boardCategoryCode)
@Slf4j
public class CheckBoardCategoryInterceptor implements HandlerInterceptor {

	private CategoryService categoryService;
	
	public CheckBoardCategoryInterceptor(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== CheckBoardCategoryInterceptor ==============");
		log.info("URL={}", getFullURL(request));
		
		String boardCategoryCode = request.getParameter("boardCategoryCode");
		
		if (!StringUtils.isNumeric(boardCategoryCode)) {
			request.getRequestDispatcher("/error/errorPage").forward(request, response);
			return false;
		}
		
		if (!categoryService.isBoardCategory(Integer.parseInt(boardCategoryCode))) {
			request.getRequestDispatcher("/error/errorPage").forward(request, response);
			return false;
		}
		
		return true;
	}

//	private boolean isNumeric(String param) {
//		try {
//			Integer.parseInt(param);
//			return true;
//		} catch (NumberFormatException e) {
//			return false; 
//		}
//	}
	
	private String getFullURL(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	    	return requestURL.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}

}
