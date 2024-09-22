package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	Integer saveBoard(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(Integer boardIdx, Integer memberIdx);
	
	void updateBoardViews(Integer boardIdx);
	
	List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri, Integer memberIdx);

	Pagination getPagination(int boardCategoryCode, Criteria boardCri);
	
	BoardDto getModifyBoard(Integer boardIdx, Integer memberIdx);
	
	Integer updateBoard(BoardDto boardDto);
	
	int getBoardCategoryCode(Integer boardIdx);
	
	int deleteBoard(Integer boardIdx, Integer memberIdx);
	
}
