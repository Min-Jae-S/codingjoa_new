package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	Board saveBoard(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(int boardId, Integer userId);
	
	void updateBoardView(int boardIdx);
	
	List<BoardDetailsDto> getPagedBoard(int categoryCode, BoardCriteria boardCri, Integer userId);

	Pagination getPagination(int categoryCode, BoardCriteria boardCri);
	
	BoardDto getModifyBoard(int boardId, int userId);
	
	Board modifyBoard(BoardDto boardDto);
	
	int getBoardCategoryCode(int boardId);
	
	Board deleteBoard(int boardId, int userId);
	
}
