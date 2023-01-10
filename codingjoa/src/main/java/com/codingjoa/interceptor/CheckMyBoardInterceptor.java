package com.codingjoa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckMyBoardInterceptor implements HandlerInterceptor {

	private BoardService boardService;
	
	public CheckMyBoardInterceptor(BoardService boardService) {
		this.boardService = boardService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== CheckMyBoardInterceptor ==============");

		String boardIdx = request.getParameter("boardIdx");
		
		if (!isNumeric(boardIdx) || !boardService.isMyBoard(Integer.parseInt(boardIdx), getCurrentWriterIdx())) {
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
	
	private Integer getCurrentWriterIdx() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) return null;

		Object principal = auth.getPrincipal();
		Integer currentWriterIdx = null;

		if (principal instanceof UserDetailsDto) {
			UserDetailsDto userDetailsDto = (UserDetailsDto) principal;
			currentWriterIdx = userDetailsDto.getMember().getMemberIdx();
		} else if (principal instanceof String) {
			currentWriterIdx = null; // principal = anonymousUser
		}

		return currentWriterIdx;
	}

}
