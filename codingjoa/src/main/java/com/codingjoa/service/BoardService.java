package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.ModifyBoardDto;
import com.codingjoa.dto.WriteBoardDto;

public interface BoardService {
	
	int uploadTempImage(String uploadFilename);
	
	int writeBoard(WriteBoardDto writeBoardDto);
	
	boolean isTempImageUploaded(int uploadIdx);
	
	void activateTempImage(WriteBoardDto writeBoardDto);
	
	BoardDetailsDto getBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<BoardDetailsDto> getBoardDetailsList(int categoryCode);
	
	boolean isBoardIdxExist(int boardIdx);
	
	void mapModifyBoard(int boardIdx, ModifyBoardDto modifyBoardDto);
	
	boolean isMyBoard(int boardIdx, int boardWriterIdx);
}
