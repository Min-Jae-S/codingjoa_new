package com.codingjoa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckBoardIdxInterceptor implements HandlerInterceptor {

	@Autowired
	private BoardService boardService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== CheckBoardIdxInterceptor ==============");

		String boardIdx = request.getParameter("boardIdx");

		if (!isNumeric(boardIdx) || !boardService.isBoardIdxExist(Integer.parseInt(boardIdx))) {
			request.getRequestDispatcher(request.getContextPath() + "/error/errorPage").forward(request, response);
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
