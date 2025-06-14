package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.entity.Comment;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.RedisKeyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	private final BoardService boardService;
	private final RedisService redisService;
	private final int pageRange;
	
	public CommentServiceImpl(CommentMapper commentMapper, BoardService boardService, RedisService redisService,
			@Value("${pagination.pageRange}") int pageRange) {
		this.commentMapper = commentMapper;
		this.boardService = boardService;
		this.redisService = redisService;
		this.pageRange = pageRange;
	}
	
	@Override
	public List<CommentDetailsDto> getPagedComments(Long boardId, CommentCriteria commentCri, Long userId) {
		log.info("\t > prior to finding pagedComments, validating existence of board");
		boardService.getBoard(boardId);
		
		log.info("\t > find pagedComments");
		List<CommentDetailsDto> pagedComments = commentMapper.findPagedComments(boardId, commentCri, userId)
				.stream()
				.map(commentDetailsmap -> {
					CommentDetailsDto commentDetails = CommentDetailsDto.from(commentDetailsmap);
					return commentDetails.isStatus() ? commentDetails : null;
				})
				.collect(Collectors.toList());

		return pagedComments;
	}
	
	@Override
	public Pagination getPagination(Long boardId, CommentCriteria commentCri) {
		int totalCnt = commentMapper.findTotalCntForPaging(boardId);
		return (totalCnt > 0) ? new Pagination(totalCnt, commentCri.getPage(), commentCri.getRecordCnt(), pageRange) : null;
	}

	@Override
	public void saveComment(CommentDto commentDto) {
		log.info("\t > prior to inserting comment, validating existence of board");
		boardService.getBoard(commentDto.getBoardId());
		//Board board = boardService.getBoard(commentDto.getBoardId());
		
		Comment comment = commentDto.toEntity();
		boolean isSaved = commentMapper.insertComment(comment);
		if (!isSaved) {
			throw new ExpectedException("error.comment.save");
		}
		
		/* additional update of the denormalized count column */
		
		// 1) non-atomic update (UPDATE ... SET comment_count = #{commentCount})
		//int count = board.getCommentCount() + 1;
		//boardService.updateCommentCount(count, comment.getBoardId());
		
		// 2) atomic update (UPDATE ... SET comment_count = comment_count + 1)
		//boardService.increaseCommentCount(comment.getBoardId());
		
		// 3) update using redis + scheduler
		String key = RedisKeyUtils.createCommentCountKey(comment.getBoardId());
		redisService.applyDelta(key, 1);
	}

	@Override
	public void updateComment(CommentDto commentDto) {
		Comment comment = getComment(commentDto.getId());
		if (!comment.getStatus()) {
			throw new ExpectedException("error.comment.alreadyDeleted");
		}
		
		if (comment.getUserId() != commentDto.getUserId()) {
			throw new ExpectedException("error.comment.notWriter");
		}
		
		Comment modifyComment = commentDto.toEntity();
		boolean isUpdated = commentMapper.updateComment(modifyComment);
		if (!isUpdated) {
			throw new ExpectedException("error.comment.update");
		}
	}
	
	@Override
	public void deleteComment(Long commentId, Long userId) {
		Comment comment = getComment(commentId);
		if (!comment.getStatus()) {
			throw new ExpectedException("error.comment.alreadyDeleted");
		}
		
		if (!comment.getUserId().equals(userId)) { // !Objects.equals(userId, comment.getUserId())
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
		
		//boardService.decreaseCommentCount(comment.getBoardId());
		String key = RedisKeyUtils.createCommentCountKey(comment.getBoardId());
		redisService.applyDelta(key, -1);
	}

	@Override
	public Comment getComment(Long commentId) {
		Comment comment = commentMapper.findCommentById(commentId);
		if (comment == null) {
			throw new ExpectedException("error.comment.notFound");
		}
		
		return comment;
	}

	@Override
	public void increaseLikeCount(Long commentId) {
		log.info("\t > increase like count");
		commentMapper.increaseLikeCount(commentId);
	}

	@Override
	public void decreaseLikeCount(Long commentId) {
		log.info("\t > decrease like count");
		commentMapper.decreaseLikeCount(commentId);
	}

	@Override
	public void applyLikeCountDelta(Integer countDelta, Long commentId) {
		log.info("\t > apply like count delta");
		commentMapper.applyLikeCountDelta(countDelta, commentId);
	}
	
}
