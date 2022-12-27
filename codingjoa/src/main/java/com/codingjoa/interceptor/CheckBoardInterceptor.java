package com.codingjoa.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.CategoryService;
import com.codingjoa.util.MessageUtils;

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
		
		if(!isNumeric(categoryCode) || !categoryService.isBoardCategory(Integer.parseInt(categoryCode))) {
			response.setContentType("text/html;charset=utf-8");
			
			String referer = request.getHeader("Referer");
			log.info("referer = {}", referer);
			
			String origin = request.getHeader("Origin");
			log.info("origin = {}", origin);
			
			String redirectUrl = referer == null ? request.getContextPath() : referer;
			log.info("redirectUrl = {}", redirectUrl);
			
			PrintWriter out = response.getWriter();
			out.print("<script>");
			out.print("alert('" + MessageUtils.getMessage("error.NotBoard") + "');");
			out.print("location.href='" +  redirectUrl + "';");
			out.print("</script>");
			out.close();
			
			return false;
		}
		
		return true;
	}
	
	private boolean isNumeric(String categoryCode) {
		try {
			Integer.parseInt(categoryCode);
			return true;
		} catch (NumberFormatException e) {
			log.info("NumberFormatException");
			return false;
		}
	}
	
}
