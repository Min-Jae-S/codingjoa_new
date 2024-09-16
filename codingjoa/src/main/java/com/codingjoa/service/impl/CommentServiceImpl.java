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

	private final CommentMapper commentMapper;
	private final BoardMapper boardMapper;
	private final ModelMapper modelMapper;
	private final int pageRange;
	
	@Autowired
	public CommentServiceImpl(CommentMapper commentMapper, BoardMapper boardMapper, ModelMapper modelMapper,
			@Value("${pagination.pageRange}") int pageRange) {
		this.commentMapper = commentMapper;
		this.boardMapper = boardMapper;
		this.modelMapper = modelMapper;
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
			Boolean dbCommentUse = (Boolean) commentDetailsMap.get("commentUse");
			if (!dbCommentUse) {
				Integer dbCommentIdx = (Integer) commentDetailsMap.get("commentIdx");
				deletedComments.add(dbCommentIdx);
				pagedComment.add(null);
			} else {
				pagedComment.add(modelMapper.map(commentDetailsMap, CommentDetailsDto.class));
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
		log.info("\t > find commentDetailsMap = {}", commentDetailsMap);
		
		if (commentDetailsMap == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		Boolean dbCommentUse = (Boolean) commentDetailsMap.get("commentUse");
		Integer dbMemberIdx = (Integer) commentDetailsMap.get("memberIdx");
		log.info("\t > dbCommentUse = {}", dbCommentUse);
		log.info("\t > dbMemberIdx = {}, memberIdx = {}", dbMemberIdx, memberIdx);
		
		if (!dbCommentUse) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (dbMemberIdx != memberIdx) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		return modelMapper.map(commentDetailsMap, CommentDetailsDto.class);
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
		log.info("\t > dbCommentUse = {}", comment.getCommentUse());
		log.info("\t > dbCommentWriterIdx = {}, commentWriterIdx = {}", comment.getMemberIdx(), commentDto.getMemberIdx());
		
		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getMemberIdx() != commentDto.getMemberIdx()) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		//comment.setCommentContent(commentDto.getCommentContent());
		log.info("\t > set up modifyComment using the found comment");
		
		commentMapper.updateComment(comment);
	}
	
	@Override
	public void deleteComment(int commentIdx, int memberIdx) {
		Comment comment = getCommentByIdx(commentIdx);
		log.info("\t > dbCommentUse = {}", comment.getCommentUse());
		log.info("\t > dbMemberIdx = {}, memberIdx = {}", comment.getMemberIdx(), memberIdx);
		
		if (!comment.getCommentUse()) {
			throw new ExpectedException("error.AlreadyDeletedComment");
		}
		
		if (comment.getMemberIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyComment");
		}
		
		//comment.setCommentUse(false);
		commentMapper.deleteComment(comment);
	}

}
