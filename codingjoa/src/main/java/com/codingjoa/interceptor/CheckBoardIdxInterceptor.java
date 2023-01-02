package com.codingjoa.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.BoardService;
import com.codingjoa.util.MessageUtils;

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
			response.setContentType("text/html;charset=utf-8");

			String referer = request.getHeader("Referer");
			log.info("referer={}", referer);
			
			String redirectUrl = referer != null ? referer : request.getContextPath();
			log.info("redirectUrl={}", redirectUrl);

			PrintWriter out = response.getWriter();
			out.print("<script>");
			out.print("alert('" + MessageUtils.getMessage("error.NotBoardIdx") + "');");
			out.print("location.href='" + redirectUrl + "';");
			out.print("</script>");
			out.close();

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
