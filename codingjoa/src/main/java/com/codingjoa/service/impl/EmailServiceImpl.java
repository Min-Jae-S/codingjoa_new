package com.codingjoa.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.codingjoa.enums.MailType;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.service.EmailService;
import com.codingjoa.test.Sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	
	@Async // must return void, Future, CompletableFuture
	@Override
	public void send(String to, MailType mailType, String value) {
		log.info("## {}.send ({})", this.getClass().getSimpleName(), Thread.currentThread().getName());
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailHelper;
		try {
			mailHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mailHelper.setTo(to);
			mailHelper.setSubject(getSubject(mailType));
			
			String html = buildTemplate(mailType, value);
			mailHelper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
			
			// MessagingException은 checked exception이라 @Async 메서드에서 발생해도 AsyncUncaughtExceptionHandler에서 처리되지 않음
			// 따라서 RuntimeException으로 wrapping하여 비동기 예외 처리 핸들러에서 감지 가능하도록 함
			throw new RuntimeException("error occurred while sending the email", e);
		}
			
		//redisService.save(email, authCode);
		//return authCode; because @Async --> null
	}
	
	private String getSubject(MailType mailType) {
		switch (mailType) {
		case JOIN:
			return "[CodingJoa] 회원가입 안내";
			
		case EMAIL_UPDATE:
			return "[CodingJoa] 이메일 변경 안내";
			
		case PASSWORD_RESET:
			return "[CodingJoa] 비밀번호 재설정 안내";
			
		default:
			throw new IllegalArgumentException("Unsupported mail type: " + mailType);
		}
	}
	
	private String buildTemplate(MailType mailType, String value) {
		String template;
		Context context = new Context();
		
		switch (mailType) {
		case JOIN:
			template = "template/join";
			context.setVariable("authCode", value);
			break;
			
		case EMAIL_UPDATE:
			template = "template/email-update";
			context.setVariable("authCode", value);
			break;
			
		case PASSWORD_RESET:
			template = "template/password-reset";
			context.setVariable("passwordResetLink", value);
			break;
			
		default:
			throw new IllegalArgumentException("Unsupported mail type: " + mailType);
		}
		
		return templateEngine.process(template, context);
	}

	@Async
	@Override
	public void triggerAsyncEx(Sample sample, String param) {
		log.info("## {}.triggerAsyncEx ({})", this.getClass().getSimpleName(), Thread.currentThread().getName());
		log.info("\t > smaple = {}, param = {}", sample, param);
		throw new ExpectedException("asynce exception");
	}

}
