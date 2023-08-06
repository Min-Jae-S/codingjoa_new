package com.codingjoa.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.BoardImageDto;
import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.UploadService;
import com.codingjoa.util.UploadFileUtils;
import com.codingjoa.validator.UploadFileValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/upload.properties")
@RequestMapping("/api/upload")
@RestController
public class UploadRestController {
	
	@Autowired
	private UploadService uploadService;
	
	@Value("${upload.profile.path}")
	private String profilePath;
	
	@Value("${upload.profile.url}")
	private String profileUrl;
	
	@InitBinder("uploadFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new UploadFileValidator());
	}
	
	@PostMapping("/board-image")
	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) throws IllegalStateException, IOException {
		log.info("## uploadBoardImage");
		
		BoardImageDto uploadedBoardImage = uploadService.uploadBoardImage(uploadFileDto.getFile());
		log.info("\t > uploaded boardImage = {}", uploadedBoardImage);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadBoardImage")
				.data(uploadedBoardImage)
				.build());
	}

	@PostMapping("/profile-image")
	public ResponseEntity<Object> uploadProfileImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			@AuthenticationPrincipal UserDetailsDto principal, HttpServletRequest request) {
		log.info("## uploadProfileImage");
		log.info("\t > original filename = {}", uploadFileDto.getFile().getOriginalFilename());
		log.info("\t > profilePath = {}", profilePath);
		
		String profileImageName = UploadFileUtils.upload(profilePath, uploadFileDto.getFile());
		log.info("\t > profileImageName = {}", profileImageName);
		
		// upload(db) profile image
		// ...
		
		String profileImageUrl = request.getContextPath() + profileUrl + profileImageName;
		log.info("\t > profileImageUrl = {}", profileImageUrl);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadProfileImage")
				.data(new BoardImageDto(null, profileImageName, profileImageUrl))
				.build());
	}
	
}
