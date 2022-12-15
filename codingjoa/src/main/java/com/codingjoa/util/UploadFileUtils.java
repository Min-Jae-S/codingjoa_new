package com.codingjoa.util;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtils {
	
	public static String upload(String uploadPath, MultipartFile file) {
		File parentFolder = new File(uploadPath);

		if (!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		
		String uploadFilename = UUID.randomUUID() + "_" +  file.getOriginalFilename();
		File uploadFile = new File(parentFolder, uploadFilename);
		
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return uploadFilename;
	}
}
