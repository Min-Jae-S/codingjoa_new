package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface BoardService {
	
	Board saveBoard(BoardDto boardDto);
	
	BoardDetailsDto getBoardDetails(Long boardId, Long userId);
	
	List<BoardDetailsDto> getPagedBoards(int categoryCode, BoardCriteria boardCri, Long userId);

	Pagination getPagination(int categoryCode, BoardCriteria boardCri);
	
	BoardDto getModifyBoard(Long boardId, Long userId);
	
	Board modifyBoard(BoardDto boardDto);
	
	Board deleteBoard(Long boardId, Long userId);
	
	Board getBoard(Long boardId);
	
	void updateCommentCount(int count, Long boardId);
	
	void increaseCommentCount(Long boardId);

	void decreaseCommentCount(Long boardId);
	
	void applyCommentCountDelta(Integer countDelta, Long boardId);
	
	void applyLikeCountDelta(Integer countDelta, Long boardId);
	
	void applyViewCountDelta(Integer countDelta, Long boardId);
	
}
