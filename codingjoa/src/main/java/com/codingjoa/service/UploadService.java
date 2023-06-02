package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDto;

public interface UploadService {
	
	int uploadImage(String uploadFilename);
	
	boolean isImageUploaded(int uploadIdx);
	
	void activateImage(BoardDto writeBoardDto);
	
	List<Integer> getUploadIdxList(int uploadBoardIdx);
	
	void modifyUpload(BoardDto modifyBoardDto);
	
}
