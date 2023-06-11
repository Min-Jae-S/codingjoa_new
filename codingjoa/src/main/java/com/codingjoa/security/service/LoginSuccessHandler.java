package com.codingjoa.security.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > referer = {}", request.getHeader("referer"));
		log.info("\t > [BEFORE] contentType = {}", response.getContentType());
		response.setContentType(MediaType.TEXT_HTML.toString());
		log.info("\t > [AFTER ] contentType = {}", response.getContentType());
		
		PrintWriter writer = response.getWriter();
		writer.println("<script>");
		writer.println("alert('" + MessageUtils.getMessage("success.Login") + "');");
		writer.println("location.href='" +  request.getContextPath() + "';");
		writer.println("</script>");
		writer.flush();
	}

}
