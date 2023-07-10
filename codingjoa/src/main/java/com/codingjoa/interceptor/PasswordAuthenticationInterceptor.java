package com.codingjoa.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
		
		boolean passwordAuthentication = false;
		HttpSession session = request.getSession(false);
		if (session == null) {
			log.info("\t > session is null");
			passwordAuthentication = false;
		} else {
			log.info("\t > current session");
			Iterator<String> attributeNames = session.getAttributeNames().asIterator();
			while (attributeNames.hasNext()) {
				String attibuteName = attributeNames.next();
				log.info("\t\t - {} = {}", attibuteName, session.getAttribute(attibuteName));
			}
			passwordAuthentication = (boolean) session.getAttribute("PASSWORD_AUTHENTICATION");
		}
		log.info("\t > PASSWORD_AUTHENTICATION = {}", passwordAuthentication);
		
		if (passwordAuthentication) {
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
