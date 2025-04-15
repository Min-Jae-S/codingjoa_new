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
import com.codingjoa.error.ExpectedException;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	private final BoardService boardService;
	private final int pageRange;
	
	public CommentServiceImpl(CommentMapper commentMapper, BoardService boardService,
			@Value("${pagination.pageRange}") int pageRange) {
		this.commentMapper = commentMapper;
		this.boardService = boardService;
		this.pageRange = pageRange;
	}
	
	@Override
	public List<CommentDetailsDto> getPagedComments(Long boardId, CommentCriteria commentCri, Long userId) {
		log.info("\t > prior to finding pagedComments, find board");
		Board board = boardService.getBoard(boardId);
		
		log.info("\t > find pagedComments");
		List<CommentDetailsDto> pagedComments = commentMapper.findPagedComments(board.getId(), commentCri, userId)
				.stream()
				.map(commentDetailsmap -> {
					log.info("\t\t - id: {}, status: {}", commentDetailsmap.get("id"), commentDetailsmap.get("status"));
					CommentDetailsDto commentDetails = CommentDetailsDto.from(commentDetailsmap);
					return commentDetails.isStatus() ? commentDetails : null;
				})
				.collect(Collectors.toList());

		return pagedComments;
	}
	
	@Override
	public Pagination getPagination(Long boardId, CommentCriteria commentCri) {
		int totalCnt = commentMapper.findTotalCntForPaging(boardId);
		int validCnt = commentMapper.findValidCntForPaging(boardId);
		return (totalCnt > 0) ? new Pagination(totalCnt, validCnt, commentCri.getPage(), commentCri.getRecordCnt(), pageRange) : null;
	}

	@Override
	public void saveComment(CommentDto commentDto) {
		log.info("\t > prior to inserting comment, find board");
		Board board = boardService.getBoard(commentDto.getBoardId());
		
		if (board == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		Comment comment = commentDto.toEntity();
		log.info("\t > convert commentDto to comment entity = {}", comment);
		
		boolean isSaved = commentMapper.insertComment(comment);
		log.info("\t > saved comment = {}", comment);

		if (!isSaved) {
			throw new ExpectedException("error.comment.save");
		}
		
		boardService.increaseCommentCount(board.getId()); // Propagation.REQUIRES_NEW
	}

	@Override
	public void updateComment(CommentDto commentDto) {
		Comment comment = commentMapper.findCommentById(commentDto.getId());
		log.info("\t > found comment = {}", comment);
		
		if (comment == null) {
			throw new ExpectedException("error.comment.notFound");
		}
		
		if (!comment.getStatus()) {
			throw new ExpectedException("error.comment.alreadyDeleted");
		}
		
		if (comment.getUserId() != commentDto.getUserId()) {
			throw new ExpectedException("error.comment.notWriter");
		}
		
		Comment modifyComment = commentDto.toEntity();
		log.info("\t > convert commentDto to comment entity = {}", modifyComment);
		
		boolean isUpdated = commentMapper.updateComment(modifyComment);
		if (!isUpdated) {
			throw new ExpectedException("error.comment.update");
		}
	}
	
	@Override
	public void deleteComment(Long commentId, Long userId) {
		Comment comment = commentMapper.findCommentById(commentId);
		log.info("\t > found comment = {}", comment);
		
		if (comment == null) {
			throw new ExpectedException("error.comment.notFound");
		}
		
		if (!comment.getStatus()) {
			throw new ExpectedException("error.comment.alreadyDeleted");
		}
		
		if (comment.getUserId() != userId) {
			throw new ExpectedException("error.comment.notWriter");
		}
		
		Comment deleteComment = Comment.builder()
				.id(comment.getId())
				.status(false)
				.build();
		
		boolean isDeleted = commentMapper.deleteComment(deleteComment);
		if (!isDeleted) {
			throw new ExpectedException("error.comment.delete");
		}
		
		boardService.decreaseCommentCount(comment.getBoardId()); // Propagation.REQUIRES_NEW
	}

}
