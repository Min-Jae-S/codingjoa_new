package com.codingjoa.interceptor;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.exception.ExpectedException;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.MessageUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ResetPasswordInterceptor implements HandlerInterceptor {

	private RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());

		String key = request.getParameter("key");
		log.info("\t > key = {}", key);
		
		if (!keyCheck(key)) {
			String message =  MessageUtils.getMessage("error.NotFindPassword");
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
			writer.println("location.href='" +  request.getContextPath() + "/member/findPassword';");
			writer.println("</script>");
			writer.flush();
			return false;
		}
		
		return true;
	}
	
	private boolean keyCheck(String key) {
		if (key == null) {
			return false;
		}
		
		return redisService.hasKey(key);
	}
	
}