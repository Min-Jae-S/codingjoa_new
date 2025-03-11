package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	Board saveBoard(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(int boardIdx, Integer memberIdx);
	
	void updateBoardView(int boardIdx);
	
	List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, BoardCriteria boardCri, Integer memberIdx);

	Pagination getPagination(int boardCategoryCode, BoardCriteria boardCri);
	
	BoardDto getModifyBoard(int boardIdx, int memberIdx);
	
	Board modifyBoard(BoardDto boardDto);
	
	int getBoardCategoryCode(int boardIdx);
	
	Board deleteBoard(int boardIdx, int memberIdx);
	
}
