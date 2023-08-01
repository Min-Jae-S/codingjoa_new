package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDto;

public interface UploadService {
	
	int uploadBoardImage(String filename);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(BoardDto boardDto);
	
	List<Integer> getUploadIdxList(int uploadBoardIdx);
	
	void deactivateBoardImage(BoardDto boardDto);
	
}
