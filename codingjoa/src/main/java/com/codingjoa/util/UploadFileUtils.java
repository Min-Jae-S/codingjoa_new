package com.codingjoa.util;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtils {
	
	public static String upload(String path, MultipartFile file) {
		File uploadFolder = new File(path);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		
		String uploadFilename = createFilename(file.getOriginalFilename());
		File saveFile = new File(uploadFolder, uploadFilename);
		try {
			file.transferTo(saveFile);  
		} catch (Exception e) { // IOException, IllegalStateException
			e.printStackTrace();
		}
		
		return uploadFilename;
	}
	
	private static String createFilename(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}
}
