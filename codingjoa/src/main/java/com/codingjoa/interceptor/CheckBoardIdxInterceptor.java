package com.codingjoa.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
		log.info("{}", getFullURL(request));

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
