package com.codingjoa.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.codingjoa.enumclass.MailType;
import com.codingjoa.service.EmailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	
	@Async
	@Override
	public void send(String to, MailType mailType, String value) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mailHelper.setTo(to);
			mailHelper.setSubject(getSubject(mailType));
			
			String html = buildTemplate(mailType, value);
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		//redisService.save(memberEmail, authCode);
		//return authCode; because @Async --> null
	}
	
	private String getSubject(MailType mailType) {
		switch (mailType) {
		case AUTH_CODE:
			return "[CodingJoa] 이메일 인증번호";
		case PASSWORD_RESET:
			return "[CodingJoa] 비밀번호 재설정 링크";
		default:
			throw new IllegalArgumentException("Unsupported mail type: " + mailType);
		}
	}
	
	private String buildTemplate(MailType mailType, String value) {
		Context context = new Context();
		
		switch (mailType) {
		case AUTH_CODE:
			context.setVariable("authCode", value);
			return templateEngine.process("template/auth-code", context);
		case PASSWORD_RESET:
			context.setVariable("passwordResetLink", value);
			return templateEngine.process("template/password-reset", context);
		default:
			throw new IllegalArgumentException("Unsupported mail type: " + mailType);
		}
	}

}
