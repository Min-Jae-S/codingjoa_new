package com.codingjoa.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.BoardImageDto;
import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.UploadService;
import com.codingjoa.validator.UploadFileValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/upload")
@RestController
public class UploadRestController {
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@InitBinder("uploadFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new UploadFileValidator());
	}
	
	@PostMapping("/board-image")
	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadBoardImage");
		
		BoardImage boardImage = uploadService.uploadBoardImage(uploadFileDto.getFile());
		log.info("\t > uploaded boardImage = {}", boardImage);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadBoardImage")
				.data(new BoardImageDto(boardImage.getBoardImageIdx(), boardImage.getBoardImageName()))
				.build());
	}

	@PostMapping("/profile-image")
	public ResponseEntity<Object> uploadProfileImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			@AuthenticationPrincipal UserDetailsDto principal, HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadProfileImage");
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		uploadService.uploadProfileImage(uploadFileDto.getFile(), memberIdx);
		
		String memberId = principal.getMember().getMemberId();
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadProfileImage")
				.build());
	}
	
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(newAuthentication);
		
		//HttpSession session = request.getSession(true);
	    //session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}
	
}
