package com.codingjoa.controller;

import java.util.Map;

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
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.service.UploadService;
import com.codingjoa.util.UploadFileUtils;
import com.codingjoa.validator.UploadFileValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/upload.properties")
@RequestMapping("/upload")
@RestController
public class UploadRestController {
	
	@Autowired
	private UploadService uploadService;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Value("${upload.url}")
	private String uploadUrl;
	
	@InitBinder("uploadFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new UploadFileValidator());
	}
	
	@PostMapping("/image")
	public ResponseEntity<Object> uploadImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			HttpServletRequest request) {
		log.info("## uploadImage");
		log.info("\t > original file name = {}", uploadFileDto.getFile().getOriginalFilename());
		
		String uploadFilename = UploadFileUtils.upload(uploadPath, uploadFileDto.getFile());
		log.info("\t > uploadFilename = {}", uploadFilename);
		
		int uploadIdx = uploadService.uploadImage(uploadFilename);
		log.info("\t > uploadIdx = {}", uploadIdx);
		
		String uploadFileUrl = request.getContextPath() + uploadUrl + uploadFilename;
		log.info("\t > uploadFileUrl = {}", uploadFileUrl);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.uploadImage")
				.data(Map.of("uploadIdx", uploadIdx, "uploadFileUrl", uploadFileUrl, "uploadFilename", uploadFilename))
				.build());
	}
	
}
