package com.codingjoa.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.BoardImage;

public interface ImageService {
	
	BoardImage uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException;
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImages(BoardDto boardDto);
	
	void modifyBoardImages(BoardDto boardDto);
	
	void updateMemberImage(MultipartFile file, Integer memberIdx);
	
}
