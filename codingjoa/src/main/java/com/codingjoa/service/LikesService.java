package com.codingjoa.service;

import com.codingjoa.dto.BoardLikesDto;
import com.codingjoa.dto.CommentLikesDto;

public interface LikesService {

	Integer toggleBoardLikes(BoardLikesDto boardLikesDto);
	
	Integer toggleCommentLikes(CommentLikesDto commentLikesDto);
	
	boolean isBoardLikes(Integer boardIdx, Integer memberIdx);
	
	boolean isCommentLikes(Integer commentIdx, Integer memberIdx);
	
	int getBoardLikesCnt(Integer boardIdx);

	int getCommentLikesCnt(Integer commentIdx);
}
