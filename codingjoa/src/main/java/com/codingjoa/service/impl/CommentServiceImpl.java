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

	private final CommentMapper replyMapper;
	private final BoardMapper boardMapper;
	private final int pageRange;
	
	public CommentServiceImpl(CommentMapper replyMapper, BoardMapper boardMapper,
			@Value("${pagination.pageRange}") int pageRange) {
		this.replyMapper = replyMapper;
		this.boardMapper = boardMapper;
		this.pageRange = pageRange;
	}

	@Override
	public void saveReply(CommentDto replyDto) {
		Board board = boardMapper.findBoardById(replyDto.getBoardId());
		log.info("\t > prior to inserting reply, find board first");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Comment reply = replyDto.toEntity();
		log.info("\t > convert replyDto to reply entity = {}", reply);
		
		boolean isSaved = replyMapper.insertReply(reply);
		log.info("\t > saved reply = {}", reply);

		if (!isSaved) {
			throw new ExpectedException("error.SaveReply");
		}
	}
	
	@Override
	public List<CommentDetailsDto> getPagedReplies(long boardId, CommentCriteria replyCri, long userId) {
		Board board = boardMapper.findBoardById(boardId);
		log.info("\t > prior to finding pagedReplies, find board first");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		log.info("\t > find pagedReplies");
		List<CommentDetailsDto> pagedReplies = replyMapper.findPagedReplies(boardId, replyCri, userId)
				.stream()
				.map(replyDetailsMap -> {
					CommentDetailsDto replyDetails = CommentDetailsDto.from(replyDetailsMap);
					return replyDetails.isStatus() ? replyDetails : null;
				})
				.collect(Collectors.toList());

		return pagedReplies;
	}
	
	@Override
	public Pagination getPagination(long boardId, CommentCriteria replyCri) {
		int totalCnt = replyMapper.findReplyTotalCnt(boardId);
		return (totalCnt > 0) ? new Pagination(totalCnt, replyCri.getPage(), replyCri.getRecordCnt(), pageRange) : null;
	}

	@Override
	public void updateReply(CommentDto replyDto) {
		Comment reply = replyMapper.findReplyById(replyDto.getId());
		log.info("\t > find reply = {}", reply);
		
		if (reply == null) {
			throw new ExpectedException("error.NotFoundReply");
		}
		
		if (!reply.getStatus()) {
			throw new ExpectedException("error.AlreadyDeletedReply");
		}
		
		if (reply.getUserId() != replyDto.getUserId()) {
			throw new ExpectedException("error.NotMyReply");
		}
		
		Comment modifyReply = replyDto.toEntity();
		log.info("\t > convert replyDto to reply entity = {}", modifyReply);
		
		boolean isUpdated = replyMapper.updateReply(modifyReply);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateReply");
		}
	}
	
	@Override
	public void deleteReply(long replyId, long userId) {
		Comment reply = replyMapper.findReplyById(replyId);
		log.info("\t > find reply = {}", reply);
		
		if (reply == null) {
			throw new ExpectedException("error.NotFoundReply");
		}
		
		if (!reply.getStatus()) {
			throw new ExpectedException("error.AlreadyDeletedReply");
		}
		
		if (reply.getUserId() != userId) {
			throw new ExpectedException("error.NotMyReply");
		}
		
		Comment deleteReply = Comment.builder()
				.id(reply.getId())
				.status(false)
				.build();
		
		boolean isDeleted = replyMapper.deleteReply(deleteReply);
		if (!isDeleted) {
			throw new ExpectedException("error.DeleteReply");
		}
	}

}
