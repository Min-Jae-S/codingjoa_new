package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;

public interface BoardService {
	
	int uploadTempImage(String uploadFilename);
	
	void writeBoard(BoardDto writeBoardDto);
	
	boolean isTempImageUploaded(int uploadIdx);
	
	void activateImage(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<BoardDetailsDto> getBoardDetailsList(int categoryCode);
	
	boolean isBoardIdxExist(int boardIdx);
	
	void bindModifyBoard(BoardDto modifyBoardDto);
	
	void deactivateImage(BoardDto boardDto);

	void modifyBoard(BoardDto modifyBoardDto);
	
}
