package com.codingjoa.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		Board board = boardMapper.findBoardByIdx(commentDto.getCommentBoardIdx());
		log.info("\t > prior to inserting comment, find board");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > convert commentDto to comment entity");
		log.info("\t > {}", comment);
		
		commentMapper.insertComment(comment);
		Integer dbCommentIdx = comment.getCommentIdx();
		log.info("\t > after inserting comment, commentIdx = {}", dbCommentIdx);
		
		if (dbCommentIdx == null) {
			throw new ExpectedException("error.WriteComment");
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
			Boolean dbCommentUse = (Boolean) commentDetailsMap.get("commentUse");
			if (!dbCommentUse) {
				Integer dbCommentIdx = (Integer) commentDetailsMap.get("commentIdx");
				deletedComments.add(dbCommentIdx);
				pagedComment.add(null);
			} else {
				pagedComment.add(modelMapper.map(commentDetailsMap, CommentDetailsDto.class));
			}
		}
		
		log.info("\t > deleted comments = {}", deletedComments);
		return pagedComment;
	}
	
	@Override
	public Pagination getPagination(int commentBoardIdx, CommentCriteria commentCri) {
		int totalCnt = commentMapper.findPagedCommentTotalCnt(commentBoardIdx, commentCri);
		return new Pagination(totalCnt, commentCri.getPage(), commentCri.getRecordCnt(), pageRange);
	}

	@Override
	public CommentDetailsDto getModifyComment(int commentIdx, int commentWriterIdx) {
		Map<String, Object> commentDetailsMap = commentMapper.findCommentDetails(commentIdx);
		log.info("\t > find commentDetailsMap = {}", commentDetailsMap);
		
		if (commentDetailsMap == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		Boolean dbCommentUse = (Boolean) commentDetailsMap.get("commentUse");
		Integer dbCommentWriterIdx = (Integer) commentDetailsMap.get("commentWriterIdx");
		log.info("\t > dbCommentUse = {}", dbCommentUse);
		log.info("\t > dbCommentWriterIdx = {}, commentWriterIdx = {}", dbCommentWriterIdx, commentWriterIdx);
		
		if (!dbCommentUse) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (dbCommentWriterIdx != commentWriterIdx) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		return modelMapper.map(commentDetailsMap, CommentDetailsDto.class);
	}
	
	@Override
	public void modifyComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > convert commentDto to comment entity");
		log.info("\t > {}", comment);
		
		commentMapper.updateComment(comment);
		log.info("\t > after updating comment");
		log.info("\t > DB commentIdx = {}", comment.getCommentIdx());
		log.info("\t > DB commentUse = {}", comment.getCommentUse());
		log.info("\t > My commentWriterIdx = {}, DB commentWriterIdx = {}", commentDto.getCommentWriterIdx(), comment.getCommentWriterIdx());
		
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
		log.info("\t > convert commentDto to comment entity");
		log.info("\t > {}", comment);
		
		commentMapper.deleteComment(comment);
		log.info("\t > after deleting comment");
		log.info("\t > DB commentIdx = {}", comment.getCommentIdx());
		log.info("\t > DB commentUse = {}", comment.getCommentUse());
		log.info("\t > My commentWriterIdx = {}, DB commentWriterIdx = {}", commentDto.getCommentWriterIdx(), comment.getCommentWriterIdx());
		
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
