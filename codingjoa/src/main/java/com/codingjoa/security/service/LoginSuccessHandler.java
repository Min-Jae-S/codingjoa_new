package com.codingjoa.security.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
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
		log.info("\t > current auth token = {}", 
				(authentication != null) ? authentication.getClass().getSimpleName() : authentication);

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.TEXT_HTML.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		String script = "<script>";
		script += "alert('" + MessageUtils.getMessage("success.Login") + "');";
		script += "location.href='" + request.getContextPath() + "';";
		script += "</script>";

		PrintWriter writer = response.getWriter();
		writer.write(script);
		writer.flush();
		writer.close();
	}

}
