package com.codingjoa.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.codingjoa.service.EmailService;

@EnableAsync
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Async // Async Config
	@Override
	public void sendAuthEmail(String memberEmail, String authCode) {
		String html = buildAuthTemplate(authCode);
		
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

			mailHelper.setTo(memberEmail);
			mailHelper.setSubject("[CodingJoa] 이메일 인증번호입니다.");
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		//redisService.saveAuthCode(memberEmail, authCode);
		//return authCode; because @Async --> null
	}
	
	@Async
	@Override
	public void sendFoundAccountEmail(String memberEmail, String memberId) {
		String html = buildAccountTemplate(memberId);
		
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

			mailHelper.setTo(memberEmail);
			mailHelper.setSubject("[CodingJoa] 아이디를 안내해드립니다.");
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	private String buildAuthTemplate(String authCode) {
		Context context = new Context();
		context.setVariable("authCode", authCode);
		return templateEngine.process("template/authcode-mail", context);
	}

	private String buildAccountTemplate(String memberId) {
		Context context = new Context();
		context.setVariable("memberId", memberId);
		return templateEngine.process("template/found-account-mail", context);
	}

}
