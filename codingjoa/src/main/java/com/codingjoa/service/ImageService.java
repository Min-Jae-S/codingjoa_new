package com.codingjoa.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardImageDto;

public interface ImageService {
	
	BoardImageDto saveBoardImage(MultipartFile file);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImages(List<Integer> boardImages, Integer boardIdx);
	
	void updateBoardImages(List<Integer> boardImages, Integer boardIdx); // deactivate, activate
	
	void updateMemberImage(MultipartFile file, Integer memberIdx);
	
}
