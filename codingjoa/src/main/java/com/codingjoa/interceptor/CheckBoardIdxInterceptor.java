package com.codingjoa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		log.info("requestURI={}", request.getRequestURI());

		String boardIdx = request.getParameter("boardIdx");

		if (!isNumeric(boardIdx) || !boardService.isBoardIdxExist(Integer.parseInt(boardIdx))) {
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
