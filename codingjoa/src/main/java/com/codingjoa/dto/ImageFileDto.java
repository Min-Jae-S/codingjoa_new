package com.codingjoa.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageFileDto {

	private MultipartFile file;
}
