package com.codingjoa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckBoardInterceptor implements HandlerInterceptor {

	@Autowired
	private CategoryService categoryService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== CheckBoardInterceptor ==============");

		String categoryCode = request.getParameter("categoryCode");

		if (!isNumeric(categoryCode) || !categoryService.isBoardCategory(Integer.parseInt(categoryCode))) {
			request.getRequestDispatcher("/error/errorPage").forward(request, response);
			return false;
		}

		return true;
	}

	private boolean isNumeric(String param) {
		try {
			Integer.parseInt(param);
			return true;
		} catch (NumberFormatException e) {
			return false; 
		}
	}

}
