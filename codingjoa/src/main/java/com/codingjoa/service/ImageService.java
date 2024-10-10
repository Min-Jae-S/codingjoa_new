package com.codingjoa.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardImageDto;

public interface ImageService {
	
	BoardImageDto saveBoardImage(MultipartFile file) throws IllegalStateException, IOException;
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImages(List<Integer> boardImages, Integer boardIdx);
	
	void replaceBoardImages(List<Integer> boardImages, Integer boardIdx); // deactivate + activate
	
	void replaceMemberImage(MultipartFile file, Integer memberIdx);
	
}
