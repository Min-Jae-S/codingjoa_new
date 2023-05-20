package com.codingjoa.interceptor;

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
		
		List<Category> parentCategoryList = categoryService.findParentCategoryList();
		request.setAttribute("parentCategoryList", parentCategoryList);
		
		return true;
	}
	

	
}
