package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	Integer saveBoard(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(int boardIdx, Integer memberIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri, Integer memberIdx);

	Pagination getPagination(int boardCategoryCode, Criteria boardCri);
	
	BoardDto getModifyBoard(int boardIdx, int memberIdx);
	
	Integer updateBoard(BoardDto boardDto);
	
	int getBoardCategoryCode(int boardIdx);
	
	int deleteBoard(int boardIdx, int memberIdx);
	
}
