package com.codingjoa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

// "/board/read"
@Slf4j
public class CheckBoardIdxInterceptor implements HandlerInterceptor {

	private BoardService boardService;
	
	public CheckBoardIdxInterceptor(BoardService boardService) {
		this.boardService = boardService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== CheckBoardIdxInterceptor ==============");

		String boardIdx = request.getParameter("boardIdx");
		
		if (!StringUtils.isNumeric(boardIdx)) {
			request.getRequestDispatcher("/error/errorPage").forward(request, response);
			return false;
		}
		
		if (!boardService.isBoardIdxExist(Integer.parseInt(boardIdx))) {
			request.getRequestDispatcher("/error/errorPage").forward(request, response);
			return false;
		}

		return true;
	}

}
