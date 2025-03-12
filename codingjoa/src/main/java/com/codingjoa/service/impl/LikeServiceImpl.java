package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.Reply;
import com.codingjoa.entity.ReplyLike;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.mapper.ReplyMapper;
import com.codingjoa.mapper.LikeMapper;
import com.codingjoa.service.LikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likesMapper;
	private final BoardMapper boardMapper;
	private final ReplyMapper commentMapper;
	
	@Override
	public boolean toggleBoardLikes(int boardIdx, int memberIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > prior to toggling boardLikes, find board");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		BoardLike boardLikes = likesMapper.findBoardLikes(boardIdx, memberIdx);
		log.info("\t > to check whether the board is liked or not, find boardLikes");
		
		if (boardLikes == null) {
			log.info("\t > insert boardLikes");
			likesMapper.insertBoardLikes(boardIdx, memberIdx);
			return true;
		} else {
			log.info("\t > delete boardLikes");
			likesMapper.deleteBoardLikes(boardLikes);
			return false;
		}
	}
	
	@Override
	public boolean toggleCommentLikes(int commentIdx, int memberIdx) {
		Reply comment = commentMapper.findCommentByIdx(commentIdx);
		log.info("\t > prior to toggling commentLikes, find comment");
		
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		ReplyLike commentLikes = likesMapper.findCommentLikes(commentIdx, memberIdx);
		log.info("\t > to check whether the comment is liked or not, find commentLikes");
		
		if (commentLikes == null) {
			log.info("\t > insert commentLikes");
			likesMapper.insertCommentLikes(commentIdx, memberIdx);
			return true;
		} else {
			log.info("\t > delete commentLikes");
			likesMapper.deleteCommentLikes(commentLikes);
			return false;
		}
	}

	@Override
	public int getBoardLikesCnt(int boardIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return likesMapper.findBoardLikesCnt(boardIdx);
	}

	@Override
	public int getCommentLikesCnt(int commentIdx) {
		Reply comment = commentMapper.findCommentByIdx(commentIdx);
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		return likesMapper.findCommentLikesCnt(commentIdx);
	}
}
