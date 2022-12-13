package com.codingjoa.validator;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.UploadFileDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "uploadFileValidator")
public class UploadFileValidator implements Validator {

	private static long MAX_FILE_SIZE = 10 * 1024 * 1024;
	
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
			errors.rejectValue("file", "Required");
			return;
		}
		
		Tika tika = new Tika();
		try {
			String mimeType = tika.detect(file.getInputStream());
			
			if (!isPermittedMimeType(mimeType)) {
				errors.rejectValue("file", "InvalidType");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (file.getSize() > MAX_FILE_SIZE) {
			errors.rejectValue("file", "ExceededSize");
			return;
		}
	}
	
	private boolean isPermittedMimeType(String mimeType) {
		if(!StringUtils.hasText(mimeType)) {
			return false;
		}
		
		if(!mimeType.startsWith("image")) {
			return false;
		}
		
		return true;
	}

}
