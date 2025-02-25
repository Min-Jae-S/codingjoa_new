package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.entity.Board;
import com.codingjoa.entity.Comment;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	private final BoardMapper boardMapper;
	private final int pageRange;
	
	public CommentServiceImpl(CommentMapper commentMapper, BoardMapper boardMapper,
			@Value("${pagination.pageRange}") int pageRange) {
		this.commentMapper = commentMapper;
		this.boardMapper = boardMapper;
		this.pageRange = pageRange;
	}

	@Override
	public void saveComment(CommentDto commentDto) {
		Board board = boardMapper.findBoardByIdx(commentDto.getBoardIdx());
		log.info("\t > prior to inserting comment, find board");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Comment comment = commentDto.toEntity();
		log.info("\t > convert commentDto to comment entity = {}", comment);
		
		boolean isSaved = commentMapper.insertComment(comment);
		log.info("\t > saved comment = {}", comment);

		if (!isSaved) {
			throw new ExpectedException("error.SaveComment");
		}
		
	}
	
	@Override
	public List<CommentDetailsDto> getPagedComment(int boardIdx, CommentCriteria commentCri, Integer memberIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > prior to finding pagedComment, find board");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		log.info("\t > find pagedComment");
		List<CommentDetailsDto> pagedComment = commentMapper.findPagedComment(boardIdx, commentCri, memberIdx)
				.stream()
				.map(commentDetailsMap -> {
					CommentDetailsDto commentDetails = CommentDetailsDto.from(commentDetailsMap);
					return commentDetails.isCommentInUse() ? commentDetails : null;
				})
				.collect(Collectors.toList());

		return pagedComment;
	}
	
	@Override
	public Pagination getPagination(int boardIdx, CommentCriteria commentCri) {
		int totalCnt = commentMapper.findCommentTotalCnt(boardIdx);
		return (totalCnt > 0) ? new Pagination(totalCnt, commentCri.getPage(), commentCri.getRecordCnt(), pageRange) : null;
	}

	@Override
	public void updateComment(CommentDto commentDto) {
		Comment comment = commentMapper.findCommentByIdx(commentDto.getCommentIdx());
		log.info("\t > find comment = {}", comment);
		
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getCommentWriterIdx() != commentDto.getCommentWriterIdx()) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		Comment modifyComment = commentDto.toEntity();
		log.info("\t > convert commentDto to comment entity = {}", modifyComment);
		
		boolean isUpdated = commentMapper.updateComment(modifyComment);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateComment");
		}
	}
	
	@Override
	public void deleteComment(int commentIdx, int memberIdx) {
		Comment comment = commentMapper.findCommentByIdx(commentIdx);
		log.info("\t > find comment = {}", comment);
		
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getCommentWriterIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		Comment deleteComment = Comment.builder()
				.commentIdx(comment.getCommentIdx())
				.commentUse(false)
				.build();
		
		boolean isDeleted = commentMapper.deleteComment(deleteComment);
		if (!isDeleted) {
			throw new ExpectedException("error.DeleteComment");
		}
	}

}
