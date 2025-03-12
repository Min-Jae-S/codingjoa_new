package com.codingjoa.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardImageDto;

public interface ImageService {
	
	BoardImageDto saveBoardImage(MultipartFile file);
	
	boolean isBoardImageUploaded(Long boardImageId);
	
	void activateBoardImages(List<Long> boardImages, Long boardId);
	
	void updateBoardImages(List<Long> boardImages, Long boardId); // deactivate, activate
	
	void updateUserImage(MultipartFile file, Long userId);
	
}
