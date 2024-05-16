package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	void writeBoard(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	Criteria createNewBoardCri(Criteria boardCri);
	
	List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri);

	Pagination getPagination(int boardCategoryCode, Criteria boardCri);
	
	BoardDto getModifyBoard(int boardIdx, int boardWriterIdx);
	
	void modifyBoard(BoardDto boardDto);
	
	int getBoardCategoryCode(int boardIdx);
	
	int deleteBoard(int boardIdx, int boardWriterIdx);
	
}
