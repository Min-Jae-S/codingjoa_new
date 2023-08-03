package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.dto.UploadResultDto;
import com.codingjoa.response.SuccessResponse;
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
	
	@Value("${upload.board.path}")
	private String path;
	
	@Value("${upload.board.url}")
	private String url;
	
	@InitBinder("uploadFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new UploadFileValidator());
	}
	
	@PostMapping("/board-image")
	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) {
		log.info("## uploadBoardImage");
		log.info("\t > original filename = {}", uploadFileDto.getFile().getOriginalFilename());
		
		String filename = UploadFileUtils.upload(path, uploadFileDto.getFile());
		log.info("\t > new filename = {}", filename);
		
		int boardImageIdx = uploadService.uploadBoardImage(filename);
		log.info("\t > idx = {}", boardImageIdx);
		
		String uploadUrl = request.getContextPath() + url + filename;
		log.info("\t > url = {}", uploadUrl);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadBoardImage")
				.data(new UploadResultDto(boardImageIdx, uploadUrl, filename))
				.build());
	}

	@PostMapping("/profile-image")
	public ResponseEntity<Object> uploadProfileImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) {
		log.info("## uploadProfileImage");
		log.info("\t > original filename = {}", uploadFileDto.getFile().getOriginalFilename());
		
		String filename = UploadFileUtils.upload(path, uploadFileDto.getFile());
		log.info("\t > new filename = {}", filename);
		
		int boardImageIdx = uploadService.uploadBoardImage(filename);
		log.info("\t > idx = {}", boardImageIdx);
		
		String uploadUrl = request.getContextPath() + url + filename;
		log.info("\t > url = {}", uploadUrl);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadProfileImage")
				.data(new UploadResultDto(boardImageIdx, uploadUrl, filename))
				.build());
	}
	
}
