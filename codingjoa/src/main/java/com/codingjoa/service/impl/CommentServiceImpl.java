package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.entity.Comment;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.service.CommentService;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public void writeComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > writeCommentDto ==> {}", comment);
		
		commentMapper.insertComment(comment);
		Integer DBcommentBoardIdx = comment.getCommentBoardIdx();
		log.info("\t > DB commentBoardIdx = {}", DBcommentBoardIdx);
		
		if (DBcommentBoardIdx == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.WriteComment"));
		}
	}
	
	@Override
	public List<CommentDetailsDto> getPagedComment(int boardIdx, CommentCriteria commentCri) {
		log.info("\t > find pagedComment");
		return commentMapper.findPagedComment(boardIdx, commentCri)
				.stream()
				.map(commentDetailsMap -> {
					Boolean commentUse = (Boolean) commentDetailsMap.get("commentUse");
					if (!commentUse) { 
						log.info("\t > not used comment, commentIdx = {}", commentDetailsMap.get("commentIdx"));
						return null;
					} 
					return modelMapper.map(commentDetailsMap, CommentDetailsDto.class);
					//return commentUse ? modelMapper.map(commentDetailsMap, CommentDetailsDto.class) : null;
				})
				.collect(Collectors.toList());
	}

	@Override
	public CommentDetailsDto getCommentDetails(int commentIdx, int commentWriterIdx) {
		Map<String, Object> commentDetailsMap = commentMapper.findCommentDetails(commentIdx);
		log.info("\t > find commentDetails, {}", commentDetailsMap);
		
		if (commentDetailsMap == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundComment"));
		}
		
		Boolean commentUse = (Boolean) commentDetailsMap.get("commentUse");
		if (!commentUse) {
			log.info("\t > this comment(commentIdx = {}) is not used", commentDetailsMap.get("commentIdx"));
			throw new ExpectedException(MessageUtils.getMessage("error.AlreadyDeletedComment"));
		}
		
		Integer DBcommentWriterIdx = (Integer) commentDetailsMap.get("commentWriterIdx");
		log.info("\t > current commentWriterIdx = {}", commentWriterIdx);
		log.info("\t > DB commentWriterIdx = {}", DBcommentWriterIdx);
		
		if (DBcommentWriterIdx != commentWriterIdx) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotMyComment"));
		}
		
		return modelMapper.map(commentDetailsMap, CommentDetailsDto.class);
	}
	
	@Override
	public void modifyComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > modifyCommentDto ==> {}", comment);
		
		commentMapper.updateComment(comment);
	}
	
	@Override
	public void deleteComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("\t > deleteCommentDto ==> {}", comment);
		
		commentMapper.deleteComment(comment);
		Integer DBcommentWriterIdx = comment.getCommentWriterIdx();
		log.info("\t > current commentWriterIdx = {}", commentDto.getCommentWriterIdx());
		log.info("\t > DB commentWriterIdx = {}", DBcommentWriterIdx);
		
		if (DBcommentWriterIdx == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.DeleteComment"));
		}
		
		if (DBcommentWriterIdx != commentDto.getCommentWriterIdx()) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotMyComment"));
		}
	}
	
}
