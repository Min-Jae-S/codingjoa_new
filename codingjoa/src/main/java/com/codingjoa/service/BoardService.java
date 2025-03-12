package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	Board saveBoard(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(long boardId, Long userId);
	
	void updateBoardView(long boardId);
	
	List<BoardDetailsDto> getPagedBoards(int categoryCode, BoardCriteria boardCri, Long userId);

	Pagination getPagination(int categoryCode, BoardCriteria boardCri);
	
	BoardDto getModifyBoard(long boardId, Long userId);
	
	Board modifyBoard(BoardDto boardDto);
	
	int getBoardCategoryCode(long boardId);
	
	Board deleteBoard(long boardId, Long userId);
	
}
