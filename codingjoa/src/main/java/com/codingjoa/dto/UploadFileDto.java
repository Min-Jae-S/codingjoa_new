package com.codingjoa.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadFileDto {

	private MultipartFile file;
}
