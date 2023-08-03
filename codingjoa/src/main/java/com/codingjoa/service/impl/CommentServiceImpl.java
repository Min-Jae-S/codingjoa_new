package com.codingjoa.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${pagination.pageRange}")
	private int pageRange;
	
	@Override
	public void writeComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > writeCommentDto ==> {}", comment);
		
		commentMapper.insertComment(comment);
		log.info("\t > DB commentBoardIdx = {}", comment.getCommentBoardIdx());
		
		if (comment.getCommentBoardIdx() == null) {
			throw new ExpectedException("error.WriteComment");
		}
	}
	
	@Override
	public List<CommentDetailsDto> getPagedComment(int commentBoardIdx, CommentCriteria commentCri) {
		Board board = boardMapper.findBoardByIdx(commentBoardIdx);
		log.info("\t > prior to finding pagedComment, find board = {}", board);
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		List<Integer> deletedComment = new ArrayList<>();
		List<CommentDetailsDto> pagedComment = commentMapper.findPagedComment(commentBoardIdx, commentCri)
				.stream()
				.map(commentDetailsMap -> {
					Boolean commentUse = (Boolean) commentDetailsMap.get("commentUse");
					if (!commentUse) {
						Integer commentIdx = (Integer) commentDetailsMap.get("commentIdx");
						deletedComment.add(commentIdx);
						return null;
					}
					return modelMapper.map(commentDetailsMap, CommentDetailsDto.class);
					//return commentUse ? modelMapper.map(commentDetailsMap, CommentDetailsDto.class) : null;
				})
				.collect(Collectors.toList());
		log.info("\t > deleted comments on page {} = {}", commentCri.getPage(), deletedComment);
		
		return pagedComment;
	}
	
	@Override
	public Pagination getPagination(int commentBoardIdx, CommentCriteria commentCri) {
		int totalCnt = commentMapper.findPagedCommentTotalCnt(commentBoardIdx, commentCri);
		return new Pagination(totalCnt, commentCri.getPage(), commentCri.getRecordCnt(), pageRange);
	}

	@Override
	public CommentDetailsDto getCommentDetails(int commentIdx, int commentWriterIdx) {
		Map<String, Object> commentDetailsMap = commentMapper.findCommentDetails(commentIdx);
		log.info("\t > find commentDetails, {}", commentDetailsMap);
		
		if (commentDetailsMap == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		Boolean DBcommentUse = (Boolean) commentDetailsMap.get("commentUse");
		Integer DBcommentWriterIdx = (Integer) commentDetailsMap.get("commentWriterIdx");
		log.info("\t > DB commentUse = {}", DBcommentUse);
		log.info("\t > DB commentWriterIdx = {}", DBcommentWriterIdx);
		log.info("\t > MY commentWriterIdx = {}", commentWriterIdx);
		
		if (!DBcommentUse) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (DBcommentWriterIdx != commentWriterIdx) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		return modelMapper.map(commentDetailsMap, CommentDetailsDto.class);
	}
	
	@Override
	public void modifyComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > modifyCommentDto ==> {}", comment);
		
		commentMapper.updateComment(comment);
		log.info("\t > DB commentIdx = {}", comment.getCommentIdx());
		log.info("\t > DB commentUse = {}", comment.getCommentUse());
		log.info("\t > DB commentWriterIdx = {}", comment.getCommentWriterIdx());
		log.info("\t > MY commentWriterIdx = {}", commentDto.getCommentWriterIdx());
		
		if (comment.getCommentIdx() == null) {
			throw new ExpectedException("error.UpdateComment");
		}

		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getCommentWriterIdx() != commentDto.getCommentWriterIdx()) {
			throw new ExpectedException("error.NotMyComment");
		}
	}
	
	@Override
	public void deleteComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > deleteCommentDto ==> {}", comment);
		
		commentMapper.deleteComment(comment);
		log.info("\t > DB commentIdx = {}", comment.getCommentIdx());
		log.info("\t > DB commentUse = {}", comment.getCommentUse());
		log.info("\t > DB commentWriterIdx = {}", comment.getCommentWriterIdx());
		log.info("\t > MY commentWriterIdx = {}", commentDto.getCommentWriterIdx());
		
		if (comment.getCommentIdx() == null) {
			throw new ExpectedException("error.DeleteComment");
		}

		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getCommentWriterIdx() != commentDto.getCommentWriterIdx()) {
			throw new ExpectedException("error.NotMyComment");
		}
	}

}
