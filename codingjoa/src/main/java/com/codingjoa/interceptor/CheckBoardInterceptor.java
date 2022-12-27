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
		
		int categoryCode = Integer.parseInt(request.getParameter("categoryCode"));
		log.info("categoryCode = {}", categoryCode);
		
		if (!categoryService.isBoardCategory(categoryCode)) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter writer = response.getWriter();
			
			writer.println("<script>");
			writer.println("alert('" + MessageUtils.getMessage("error.NotBoard") + "');");
			writer.println("location.href='" +  request.getContextPath() + "/member/checkPassword';");
			writer.println("</script>");
			writer.close();
			
			String uri = request.getHeader("Referer");
			log.info("previous uri = {}", uri);
			
			return false;
		}
		
		return true;
	}

	
}
