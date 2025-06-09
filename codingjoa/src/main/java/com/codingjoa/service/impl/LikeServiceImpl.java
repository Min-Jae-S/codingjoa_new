package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardLikeDto;
import com.codingjoa.dto.CommentLikeDto;
import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.CommentLike;
import com.codingjoa.mapper.LikeMapper;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.codingjoa.service.LikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likeMapper;
	private final BoardService boardService;
	private final CommentService commentService;
	
	@Override
	public BoardLikeDto toggleBoardLike(Long boardId, Long userId) {
		log.info("\t > prior to toggling boardLike, find board");
		boardService.getBoard(boardId);
	
		log.info("\t > to check whether the board is liked or not, find boardLike");
		BoardLike boardLike = likeMapper.findBoardLike(boardId, userId);
		boolean liked;
		
		if (boardLike == null) {
			log.info("\t > insert boardLike");
			likeMapper.insertBoardLike(boardId, userId);
			liked = true;
		} else {
			log.info("\t > delete boardLike");
			likeMapper.deleteBoardLike(boardLike.getId());
			liked = false;
		}
		
		int likeCount = boardService.getLikeCount(boardId);
		
		return new BoardLikeDto(liked, likeCount);
	}
	
	@Override
	public CommentLikeDto toggleCommentLike(Long commentId, Long userId) {
		log.info("\t > prior to toggling commentLike, find comment");
		commentService.getComment(commentId);
	
		log.info("\t > to check whether the comment is liked or not, find commentLike");
		CommentLike commentLike = likeMapper.findCommentLike(commentId, userId);
		boolean liked;
		
		if (commentLike == null) {
			log.info("\t > insert commentLike");
			likeMapper.insertCommentLike(commentId, userId);
			liked = true;
		} else {
			log.info("\t > delete commentLike");
			likeMapper.deleteCommentLike(commentLike.getId());
			liked = false;
		}
		
		int likeCount = commentService.getLikeCount(commentId);
		
		return new CommentLikeDto(liked, likeCount);
	}
	
}
