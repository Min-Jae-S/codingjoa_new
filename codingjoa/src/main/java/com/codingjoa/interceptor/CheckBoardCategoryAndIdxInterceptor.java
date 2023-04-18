//package com.codingjoa.interceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import com.codingjoa.service.BoardService;
//
//import lombok.extern.slf4j.Slf4j;
//
//// "/board/read"
//@Slf4j
//public class CheckBoardCategoryAndIdxInterceptor implements HandlerInterceptor {
//
//	private BoardService boardService;
//	
//	public CheckBoardCategoryAndIdxInterceptor(BoardService boardService) {
//		this.boardService = boardService;
//	}
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		log.info("============== CheckBoardCategoryAndIdxInterceptor ==============");
//
//		String boardIdx = request.getParameter("boardIdx");
//		String boardCategoryCode = request.getParameter("boardCategoryCode");
//		
//		if (!StringUtils.isNumeric(boardIdx) || !StringUtils.isNumeric(boardCategoryCode)) {
//			request.getRequestDispatcher("/error/errorPage").forward(request, response);
//			return false;
//		}
//		
//		if (!boardService.isBoardIdxExist(Integer.parseInt(boardIdx), Integer.parseInt(boardCategoryCode))) {
//			request.getRequestDispatcher("/error/errorPage").forward(request, response);
//			return false;
//		}
//
//		return true;
//	}
//
//}
