package com.codingjoa.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.MemberImage;

public interface ImageService {
	
	BoardImage uploadBoardImage(MultipartFile file) throws IllegalStateException, IOException;
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImages(BoardDto boardDto);
	
	void modifyBoardImages(BoardDto boardDto);
	
	MemberImage uploadMemberImage(MultipartFile file, Integer memberIdx) throws IllegalStateException, IOException;
	
	BoardImage getBoardImageByIdx(Integer boardIdx); // test

	BoardImage getBoardImageByName(String boardImageName);
	
	MemberImage getMemberImageByName(String profileImageName, Integer memberIdx);
	
}
