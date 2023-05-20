package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {
	
	private CategoryService categoryService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("-------- CategoryInterceptor --------");
		log.info("\t > URI = {}", getFullURI(request));
		log.info("\t > handler = {}", (handler != null) ? handler.getClass().getSimpleName() : "No handler");
		
		List<Category> parentCategoryList = categoryService.findParentCategoryList();
		request.setAttribute("parentCategoryList", parentCategoryList);
		
		return true;
	}
	
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = new StringBuilder(request.getRequestURI().toString());
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
	
}
