package com.codingjoa.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeforeUpdatePasswordInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== BeforeUpdatePasswordInterceptor - preHandle ==============");

		if(!request.getMethod().equals("PUT")) {
			return false;
		}
		
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("CHECK_PASSWORD");

		if(obj != null && (boolean) obj) {
			session.setAttribute("CHECK_PASSWORD", false);
			return true;
		}

		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('" + MessageUtils.getMessage("error.NotCheckPassword") + "');");
		out.println("location.href='" +  request.getContextPath() + "/member/checkPassword';");
		out.println("</script>");

		return false;
	}
	
}
