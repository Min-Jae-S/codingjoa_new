package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	int uploadImage(String uploadFilename);
	
	int writeBoard(BoardDto writeBoardDto);
	
	boolean isImageUploaded(int uploadIdx);
	
	void activateImage(BoardDto writeBoardDto);
	
	BoardDetailsDto getBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	Criteria makeNewBoardCri(Criteria boardCri);
	
	List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri);

	Pagination getPagination(int boardCategoryCode, Criteria boardCri);
	
	boolean isBoardIdxExist(int boardIdx, int boardCategoryCode);
	
	void bindModifyBoard(BoardDto modifyBoardDto);
	
	boolean isMyBoard(int boardIdx, int boardWriterIdx);
	
	void modifyBoard(BoardDto modifyBoardDto);
	
	void modifyUpload(BoardDto modifyBoardDto);
	
	int getBoardCategoryCode(int boardIdx);
	
	void deleteBoard(int boardIdx);
	
}
