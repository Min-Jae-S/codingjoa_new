package com.codingjoa.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeforeShowAccountInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("============== BeforeShowAccountInterceptor - preHandle ==============");

		HttpSession session = request.getSession();
		Object obj = session.getAttribute("");

		if(obj != null && (boolean) obj) {
			session.setAttribute("", false);
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
