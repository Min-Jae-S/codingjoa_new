package com.codingjoa.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadFileDto {

	//private List<MultipartFile> file;
	private MultipartFile file;
	
}
