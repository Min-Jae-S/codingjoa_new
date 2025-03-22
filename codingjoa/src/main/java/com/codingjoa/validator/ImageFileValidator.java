package com.codingjoa.validator;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.ImageFileDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageFileValidator implements Validator {

	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

	@Override
	public boolean supports(Class<?> clazz) {
		return ImageFileDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		ImageFileDto imageFile = (ImageFileDto) target;
		MultipartFile file = imageFile.getFile();
		String originalFilename = file.getOriginalFilename();

		if (file.isEmpty()) {
			errors.rejectValue("file", "NotEmpty", new Object[] { originalFilename }, null);
			return;
		}

		Tika tika = new Tika();
		try {
			String mimeType = tika.detect(file.getInputStream());
			if (!isPermittedMimeType(mimeType)) {
				errors.rejectValue("file", "NotValidType", new Object[] { originalFilename, mimeType }, null);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long fileSize = file.getSize();
		if (fileSize > MAX_FILE_SIZE) {
			long maxSizeInMB = DataSize.ofBytes(MAX_FILE_SIZE).toMegabytes();
			long sizeInMB = DataSize.ofBytes(fileSize).toMegabytes();
			errors.rejectValue("file", "ExceededSize", new Object[] { maxSizeInMB, originalFilename, sizeInMB }, null);
			return;
		}
	}

	private boolean isPermittedMimeType(String mimeType) {
		if (!StringUtils.hasText(mimeType)) {
			return false;
		}

		if (!mimeType.startsWith("image")) {
			return false;
		}

		return true;
	}
}
