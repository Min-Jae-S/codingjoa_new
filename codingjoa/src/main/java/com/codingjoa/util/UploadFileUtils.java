package com.codingjoa.util;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtils {
	
	public static String upload(String uploadPath, MultipartFile file) {
		File uploadFolder = new File(uploadPath);

		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = UUID.randomUUID() + "_" +  file.getOriginalFilename();
		File uploadFile = new File(uploadFolder, uploadFilename);
		
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return uploadFilename;
	}
}
