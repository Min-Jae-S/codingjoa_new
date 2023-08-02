package com.codingjoa.service;

import com.codingjoa.dto.BoardDto;

public interface UploadService {
	
	int uploadBoardImage(String filename);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(BoardDto boardDto);
	
	void deactivateBoardImage(BoardDto boardDto);
	
}
