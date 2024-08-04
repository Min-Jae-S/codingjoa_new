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
	public void sendAuthCode(String memberEmail, String authCode) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			mailHelper.setTo(memberEmail);
			mailHelper.setSubject("[CodingJoa] 이메일 인증번호입니다.");
			
			String html = buildTemplate(MailType.AUTH_CODE, authCode);
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		//redisService.save(memberEmail, authCode);
		//return authCode; because @Async --> null
	}
	
	@Async
	@Override
	public void sendFoundAccount(String memberEmail, String memberId) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			mailHelper.setTo(memberEmail);
			mailHelper.setSubject("[CodingJoa] 아이디 안내 메일입니다.");
			
			String html = buildTemplate(MailType.FIND_ACCOUNT, memberId);
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Async
	@Override
	public void sendResetPasswordUrl(String memberEmail, String memberId, String url) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			
			mailHelper.setTo(memberEmail);
			mailHelper.setSubject("[CodingJoa] 비밀번호 재설정 메일입니다.");
			
			String html = buildTemplate(MailType.FIND_PASSWORD, memberId, url);
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	private String buildTemplate(MailType mailType, String... variable) {
		String template;
		Context context = new Context();
		if (mailType == MailType.AUTH_CODE) {
			context.setVariable("authCode", variable[0]);
			template = "template/auth-code-mail";
		} else if (mailType == MailType.FIND_ACCOUNT) {
			context.setVariable("memberId", variable[0]);
			template = "template/find-account-mail";
		} else { // MailType.FIND_PASSWORD
			context.setVariable("memberId", variable[0]);
			context.setVariable("resetPasswordUri", variable[1]);
			template = "template/find-password-mail";
		}
		
		return templateEngine.process(template, context);
	}
}
