package com.codingjoa.service;

import com.codingjoa.dto.BoardDto;

public interface UploadService {
	
	int uploadImage(String uploadFilename);
	
	boolean isImageUploaded(int uploadIdx);
	
	void activateImage(BoardDto writeBoardDto);
	
	void modifyUpload(BoardDto modifyBoardDto);
	
}
