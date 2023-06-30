package com.codingjoa.service;

import com.codingjoa.dto.BoardLikesDto;
import com.codingjoa.dto.CommentLikesDto;

public interface LikesService {

	void toggleBoardLikes(BoardLikesDto boardLikesDto);
	
	BoardLikesDto getBoardLikes(Integer boardIdx, Integer memberIdx);
	
	void toggleCommentLikes(CommentLikesDto commentLikesDto);
}
