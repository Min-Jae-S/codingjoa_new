package com.codingjoa.interceptor;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.exception.ExpectedException;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckPasswordAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());
		
		if (!isPasswordAuthenticated(request)) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
				throw new ExpectedException(MessageUtils.getMessage("error.NotCheckPassword"));
			}
			
			response.setContentType(MediaType.TEXT_HTML.toString());
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			
			PrintWriter writer = response.getWriter();
			writer.println("<script>");
			writer.println("alert('" + MessageUtils.getMessage("error.NotCheckPassword") + "');");
			writer.println("location.href='" +  request.getContextPath() + "/member/account/checkPassword';");
			writer.println("</script>");
			writer.flush();
			return false;
		}
		
		return true;
	}
	
	private boolean isPasswordAuthenticated(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean passwordAuthentication = (Boolean) session.getAttribute("PASSWORD_AUTHENTICATION");
		log.info("\t > PASSWORD_AUTHENTICATION = {}", passwordAuthentication);
		
		if (passwordAuthentication == null) {
			return false;
		}
		
		if (!passwordAuthentication) {
			return false;
		}
		
		return true;
	}
}
