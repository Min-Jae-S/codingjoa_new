package com.codingjoa.service;

import com.codingjoa.dto.BoardDto;

public interface UploadService {
	
	int uploadBoardImage(String boardImageName);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(BoardDto boardDto);
	
	void deactivateBoardImage(BoardDto boardDto);
	
}
