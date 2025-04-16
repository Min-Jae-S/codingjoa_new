package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.Comment;
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
	public boolean toggleBoardLike(long boardId, long userId) {
		log.info("\t > prior to toggling boardLike, find board");
		boardService.getBoard(boardId);
	
		log.info("\t > to check whether the board is liked or not, find boardLike");
		BoardLike boardLike = likeMapper.findBoardLike(boardId, userId);
		
		if (boardLike == null) {
			log.info("\t > insert boardLike");
			likeMapper.insertBoardLike(boardId, userId);
			boardService.increaseLikeCount(boardId);
			return true;
		} else {
			log.info("\t > delete boardLike");
			likeMapper.deleteBoardLike(boardLike.getId());
			boardService.decreaseLikeCount(boardId);
			return false;
		}
	}
	
	@Override
	public boolean toggleCommentLike(long commentId, long userId) {
		log.info("\t > prior to toggling commentLike, find comment");
		commentService.getComment(commentId);
	
		log.info("\t > to check whether the comment is liked or not, find commentLike");
		CommentLike commentLike = likeMapper.findCommentLike(commentId, userId);
		
		if (commentLike == null) {
			log.info("\t > insert commentLike");
			likeMapper.insertCommentLike(commentId, userId);
			commentService.increaseLikeCount(commentId);
			return true;
		} else {
			log.info("\t > delete commentLike");
			likeMapper.deleteCommentLike(commentLike.getId());
			commentService.decreaseLikeCount(commentId);
			return false;
		}
	}

	@Override
	public int getBoardLikeCnt(long boardId) {
		Board board = boardService.getBoard(boardId);
		return likeMapper.findBoardLikeCnt(board.getId());
	}

	@Override
	public int getCommentLikeCnt(long commentId) {
		Comment comment = commentService.getComment(commentId);
		return likeMapper.findCommentLikeCnt(comment.getId());
	}
}
