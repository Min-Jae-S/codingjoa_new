package com.codingjoa.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.dto.BoardImageDto;

public interface UploadService {
	
	BoardImageDto uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException;
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(BoardDto boardDto);
	
	void deactivateBoardImage(BoardDto boardDto);
	
	void uploadProfileImage(MultipartFile file, Integer memberIdx) throws IllegalStateException, IOException;
	
}
