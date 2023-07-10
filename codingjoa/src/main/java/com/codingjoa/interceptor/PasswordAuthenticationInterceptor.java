package com.codingjoa.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordAuthenticationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());
		
		HttpSession session = request.getSession();
		Boolean passwordAuthentication = (Boolean) session.getAttribute("PASSWORD_AUTHENTICATION");
		if (passwordAuthentication == null) {
			log.info("\t > NO PASSWORD_AUTHENTICATION ATTRIBUTE");
			makeResponse(request, response);
			return false;
		}
		
		if (!passwordAuthentication) {
			log.info("\t > NOT AUTHENTICATED");
			makeResponse(request, response);
			return false;
		}
		
		return true;
	}
	
	private void makeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType(MediaType.TEXT_HTML.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		PrintWriter writer = response.getWriter();
		writer.println("<script>");
		writer.println("alert('" + MessageUtils.getMessage("error.NotCheckPassword") + "');");
		writer.println("location.href='" +  request.getContextPath() + "/member/account/checkPassword';");
		writer.println("</script>");
		writer.flush();
	}

}
