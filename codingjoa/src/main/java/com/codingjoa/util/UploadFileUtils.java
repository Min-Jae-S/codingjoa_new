package com.codingjoa.util;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtils {
	
	public static String upload(String uploadPath, MultipartFile file) {
		String uploadFilename = UUID.randomUUID() + "_" +  file.getOriginalFilename();
		
		
		return uploadFilename;
	}
}
