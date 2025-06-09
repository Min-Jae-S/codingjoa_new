package com.codingjoa.service;

import com.codingjoa.dto.BoardLikeDto;
import com.codingjoa.dto.CommentLikeDto;

public interface LikeService {

	BoardLikeDto toggleBoardLike(Long boardId, Long userId);
	
	CommentLikeDto toggleCommentLike(Long commentId, Long userId);
	
}
