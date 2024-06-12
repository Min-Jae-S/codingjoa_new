package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > authentication token = {}", (authentication != null) ? authentication.getClass().getSimpleName() : authentication);
		
//		response.setStatus(HttpStatus.OK.value());
//		response.setContentType(MediaType.APPLICATION_JSON_VALUE.toString());
//		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
//		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
//		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
//				.json()
//				.timeZone(TimeZone.getTimeZone("Asia/Seoul"))
//				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
//				.build();
//		
//		String redirectUrl = request.getContextPath() + "/login";
//		log.info("\t > redirectUrl = {}", redirectUrl);
//		
//		SuccessResponse successResponse = SuccessResponse.builder()
//				.status(HttpStatus.OK)
//				.messageByCode("success.Logout")
//				.data(Map.of("redirectUrl", redirectUrl))
//				.build();
//		log.info("\t > {}", successResponse);
//		
//		log.info("\t > respond with successResponse in JSON format");
//		response.getWriter().write(objectMapper.writeValueAsString(successResponse));
//		response.getWriter().close();
		
		String redirectUrl = request.getParameter("redirect");
		log.info("\t > logout redirectUrl from parameter = {}", redirectUrl);
		
		if (!StringUtils.hasText(redirectUrl)) {
			redirectUrl = request.getContextPath();
		}
		log.info("\t > logout redirectUrl = {}", redirectUrl);
		
		redirectStrategy.sendRedirect(request, response, redirectUrl);
	}

}
