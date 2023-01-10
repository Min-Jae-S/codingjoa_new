package com.codingjoa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

// "/board/main", "/board/write"
// "/board/writeProc", "/board/modifyProc"
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
		
		String method = request.getMethod();
		String categoryCode = null;
		
		if (method.equals("GET")) {	// "/board/main", "/board/write"
			categoryCode = request.getParameter("categoryCode");
		} else if (method.equals("POST")) { // "/board/writeProc", "/board/modifyProc"
			categoryCode = request.getParameter("boardCategoryCode");
		}
		
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
