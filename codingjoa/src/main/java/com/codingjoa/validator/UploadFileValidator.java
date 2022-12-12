package com.codingjoa.validator;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.UploadFileDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "uploadFileValidator")
public class UploadFileValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UploadFileDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== UploadFileValidator ==============");
		
		UploadFileDto uploadFileDto = (UploadFileDto) target;
		MultipartFile file = uploadFileDto.getFile();
		
		if (file.isEmpty()) {
			//errors.rejectValue("boardContent", "NotExist");
			//errors.reject("...");
			log.info("file.getSize == 0");
			return;
		}
		
		Tika tika = new Tika();
		String mimeTypeA = tika.detect(file.getOriginalFilename());
		log.info("mimeType(detected by name) = {}", mimeTypeA);
		
		try {
			String mimeTypeB = tika.detect(file.getInputStream());
			log.info("mimeType(detected by input stream) = {}", mimeTypeB);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
