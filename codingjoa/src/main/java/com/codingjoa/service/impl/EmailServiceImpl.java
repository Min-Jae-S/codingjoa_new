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

import com.codingjoa.enumclass.MailType;
import com.codingjoa.service.EmailService;

@EnableAsync // Async Config
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
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
	public void sendResetPasswordUrl(String memberEmail, String memberId, String resetPasswordUrl) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			
			mailHelper.setTo(memberEmail);
			mailHelper.setSubject("[CodingJoa] 비밀번호 재설정 메일입니다.");
			
			String html = buildTemplate(MailType.FIND_PASSWORD, memberId, resetPasswordUrl);
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	private String buildTemplate(MailType mailType, String... variable) {
		Context context = new Context();
		if (mailType == MailType.AUTH_CODE) {
			context.setVariable("authCode", variable[0]);
			return templateEngine.process("template/auth-code-mail", context);
			
		} else if (mailType == MailType.FIND_ACCOUNT) {
			context.setVariable("memberId", variable[0]);
			return templateEngine.process("template/find-account-mail", context);
			
		} else { // MailType.FIND_PASSWORD
			context.setVariable("memberId", variable[0]);
			context.setVariable("resetPasswordUrl", variable[1]);
			return templateEngine.process("template/find-password-mail", context);
		}
	}
}
