package com.codingjoa.validator;

import java.util.List;

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
		List<MultipartFile> file = uploadFileDto.getFile();
		
		if (file == null) {
			errors.reject("");
			return;
		}
		
		if (file.isEmpty()) {
			//errors.rejectValue("boardContent", "NotExist");
			errors.reject("error.");
			return;
		} 
	}

}
