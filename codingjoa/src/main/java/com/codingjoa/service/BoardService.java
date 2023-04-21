package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
//	int uploadImage(String uploadFilename);				## BoardService --> UploadService
	
	int writeBoard(BoardDto writeBoardDto);
	
//	boolean isImageUploaded(int uploadIdx);				## BoardService --> UploadService
	
//	void activateImage(BoardDto writeBoardDto);			## BoardService --> UploadService
	
	BoardDetailsDto getBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	Criteria makeNewBoardCri(Criteria boardCri);
	
	List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri);

	Pagination getPagination(int boardCategoryCode, Criteria boardCri);
	
	boolean isBoardIdxExist(int boardIdx, int boardCategoryCode);
	
	void bindModifyBoard(BoardDto modifyBoardDto);
	
//	boolean isMyBoard(int boardIdx, int boardWriterIdx);
	
	void modifyBoard(BoardDto modifyBoardDto);
	
//	void modifyUpload(BoardDto modifyBoardDto);			## BoardService --> UploadService
	
	int getBoardCategoryCode(int boardIdx);
	
	int deleteBoard(BoardDto deleteBoardDto);
	
}
