package com.codingjoa.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardImageDto;

public interface ImageService {
	
	BoardImageDto uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException;
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImages(List<Integer> boardImages, Integer boardIdx);
	
	void modifyBoardImages(List<Integer> boardImages, Integer boardIdx);
	
	void uploadMemberImage(MultipartFile file, Integer memberIdx);
	
}
