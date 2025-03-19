package com.codingjoa.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardImageDto;

public interface ImageService {
	
	void saveUserImageWithUpload(MultipartFile file, long userId);
	
	BoardImageDto saveBoardImageWithUpload(MultipartFile file);
	
	boolean isBoardImageUploaded(Long boardImageId);
	
	void activateBoardImages(List<Long> boardImages, Long boardId);
	
	void updateBoardImages(List<Long> boardImages, Long boardId); // deactivate, activate
}
