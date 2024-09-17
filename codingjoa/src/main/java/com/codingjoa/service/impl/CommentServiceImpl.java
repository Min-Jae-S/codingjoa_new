package com.codingjoa.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.codingjoa.util.FormatUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	private final BoardMapper boardMapper;
	private final int pageRange;
	
	@Autowired
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
		if (!isSaved) {
			throw new ExpectedException("error.SaveComment");
		}
	}
	
	@Override
	public List<CommentDetailsDto> getPagedComment(int commentBoardIdx, CommentCriteria commentCri) {
		Board board = boardMapper.findBoardByIdx(commentBoardIdx);
		log.info("\t > prior to finding pagedComment, find board");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
//		List<CommentDetailsDto> pagedComment = commentMapper.findPagedComment(commentBoardIdx, commentCri)
//				.stream()
//				.map(commentDetailsMap -> {
//					Boolean commentUse = (Boolean) commentDetailsMap.get("commentUse");
//					return commentUse ? modelMapper.map(commentDetailsMap, CommentDetailsDto.class) : null;
//				})
//				.collect(Collectors.toList());
		
		List<Map<String, Object>> allPagedComment = commentMapper.findPagedComment(commentBoardIdx, commentCri);
		List<CommentDetailsDto> pagedComment = new ArrayList<>();
		List<Integer> deletedComments = new ArrayList<>();
		for (Map<String, Object> commentDetailsMap : allPagedComment) {
			CommentDetailsDto commentDetails = CommentDetailsDto.from(commentDetailsMap);
			if (commentDetails.isCommentUse()) {
				pagedComment.add(commentDetails);
			} else {
				pagedComment.add(null);
				deletedComments.add(commentDetails.getCommentIdx());
			}
		}
		log.info("\t > deletedComments = {}", deletedComments);
		
		return pagedComment;
	}
	
	@Override
	public Pagination getPagination(int commentBoardIdx, CommentCriteria commentCri) {
		int totalCnt = commentMapper.findPagedCommentTotalCnt(commentBoardIdx, commentCri);
		return new Pagination(totalCnt, commentCri.getPage(), commentCri.getRecordCnt(), pageRange);
	}

	@Override
	public CommentDetailsDto getModifyComment(int commentIdx, int memberIdx) {
		Map<String, Object> commentDetailsMap = commentMapper.findCommentDetailsByIdx(commentIdx);
		log.info("\t > find commentDetailsMap = {}", FormatUtils.formatFields(commentDetailsMap));
		
		if (commentDetailsMap == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		CommentDetailsDto commentDetails = CommentDetailsDto.from(commentDetailsMap);
		if (!commentDetails.isCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (commentDetails.getMemberIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		return commentDetails;
	}
	
	private Comment getCommentByIdx(Integer commentIdx) {
		Comment comment = commentMapper.findCommentByIdx(commentIdx);
		log.info("\t > find comment = {}", comment);
		
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		return comment;
	}
	
	@Override
	public void updateComment(CommentDto commentDto) {
		Comment comment = getCommentByIdx(commentDto.getCommentIdx());
		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getMemberIdx() != commentDto.getMemberIdx()) {
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
		Comment comment = getCommentByIdx(commentIdx);
		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getMemberIdx() != memberIdx) {
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
