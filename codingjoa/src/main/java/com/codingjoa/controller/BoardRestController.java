package com.codingjoa.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.error.SuccessResponse;
import com.codingjoa.service.BoardService;
import com.codingjoa.util.UploadFileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/upload.properties")
@RequestMapping("/board")
@RestController
public class BoardRestController {
	
	@Autowired
	private BoardService boardService;
	
	@Resource(name = "uploadFileValidator")
	private Validator uploadFileValidator;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Value("${upload.url}")
	private String uploadUrl;
	
	@InitBinder("uploadFileDto")
	public void initBinderJoin(WebDataBinder binder) {
		binder.addValidators(uploadFileValidator);
	}
	
	@PostMapping("/uploadTempImage")
	public ResponseEntity<Object> uploadTempImage(@ModelAttribute @Valid UploadFileDto uploadFileDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("{}", uploadFileDto);
		
		if (bindingResult.hasErrors()) {
			 throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		String uploadFilename = UploadFileUtils.upload(uploadPath, uploadFileDto.getFile());

		int uploadIdx = boardService.uploadTempImage(uploadFilename);
		log.info("uploadIdx = {}", uploadIdx);
		
		String returnUrl = uploadUrl + uploadFilename; // 	/upload/6db5c891-4f87-432d-ba13-d912a21b09d3_profile.jpg
		log.info("returnUrl = {}", returnUrl);	
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("uploadIdx", uploadIdx);
		map.put("returnUrl", returnUrl);
		
		return ResponseEntity.ok(SuccessResponse.create().data(map));
	}
	
}
