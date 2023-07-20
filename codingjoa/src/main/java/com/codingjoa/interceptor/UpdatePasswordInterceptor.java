package com.codingjoa.interceptor;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.exception.ExpectedException;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdatePasswordInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());
		
		if (!passwordCheck(request)) {
			String message =  MessageUtils.getMessage("error.NotCheckPassword");
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
				message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".<br>"), "<br>");
				log.info("\t > new message = {}", message);
				throw new ExpectedException(message);
			}
			
			response.setContentType(MediaType.TEXT_HTML.toString());
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			
			message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".\\\\n"), "\\n");
			log.info("\t > new message = {}", message);
			
			PrintWriter writer = response.getWriter();
			writer.println("<script>");
			writer.println("alert('" + message + "');");
			writer.println("location.href='" +  request.getContextPath() + "/member/account/checkPassword';");
			writer.println("</script>");
			writer.flush();
			return false;
		}
		
		return true;
	}
	
	private boolean passwordCheck(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean passwordCheck = (Boolean) session.getAttribute("CHECK_PASSWORD");
		log.info("\t > CHECK_PASSWORD = {}", passwordCheck);
		
		if (passwordCheck == null) {
			return false;
		}
		
		if (!passwordCheck) {
			return false;
		}
		
		return true;
	}
}
