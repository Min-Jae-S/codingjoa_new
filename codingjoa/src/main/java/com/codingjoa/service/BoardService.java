package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;

public interface BoardService {
	
	int uploadImage(String uploadFilename);
	
	int writeBoard(BoardDto writeBoardDto);
	
	boolean isImageUploaded(int uploadIdx);
	
	void activateImage(BoardDto writeBoardDto);
	
	BoardDetailsDto getBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	int getBoardDetailsListCnt(int categoryCode);
	
	List<BoardDetailsDto> getBoardDetailsList(int categoryCode);
	
	boolean isBoardIdxExist(int boardIdx);
	
	void bindModifyBoard(BoardDto modifyBoardDto);
	
	boolean isMyBoard(int boardIdx, int boardWriterIdx);
	
	void modifyBoard(BoardDto modifyBoardDto);
	
	void modifyUpload(BoardDto modifyBoardDto);
	
	
	
}
