package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.ImageService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.RedisKeyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	private final BoardMapper boardMapper;
	private final ImageService imageService;
	private final RedisService redisService;
	private final int pageRange;
	
	public BoardServiceImpl(BoardMapper boardMapper, ImageService imageService, RedisService redisService,
			@Value("${pagination.pageRange}") int pageRange) {
		this.boardMapper = boardMapper;
		this.imageService = imageService;
		this.redisService = redisService;
		this.pageRange = pageRange;
	}

	@Override
	public Board saveBoard(BoardDto boardDto) {
		log.info("\t > produce searchContent by parsing content for search");
		String searchContent = Jsoup.parse(boardDto.getContent()).text();
		boardDto.setSearchContent(searchContent);

		Board board = boardDto.toEntity();
		boolean isSaved = boardMapper.insertBoard(board);
		if (!isSaved) {
			throw new ExpectedException("error.board.save");
		}
		
		imageService.activateBoardImages(boardDto.getImages(), board.getId());
		
		return board;
	}

	@Override
	public BoardDetailsDto getBoardDetails(Long boardId, Long userId) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetailsById(boardId, userId);
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		addViewCountCache(boardId);
		
		BoardDetailsDto boardDetails = BoardDetailsDto.from(boardDetailsMap);
		applyCountDelta(boardDetails);
		
		return boardDetails;
	}
	
	private void addViewCountCache(Long boardId) {
		// TODO: Implement duplicate view prevention logic later
		String key = RedisKeyUtils.createViewCountKey(boardId);
		redisService.applyDelta(key, 1);
	}
	
	private void applyCountDelta(BoardDetailsDto boardDetails) {
		Long boardId = boardDetails.getId();
		int commentCountDelta = redisService.getDelta(RedisKeyUtils.createCommentCountKey(boardId));
		int likeCountDelta = redisService.getDelta(RedisKeyUtils.createBoardLikeCountKey(boardId));
		int viewCountDelta = redisService.getDelta(RedisKeyUtils.createViewCountKey(boardId));
		
		boardDetails.setCommentCount(boardDetails.getCommentCount() + commentCountDelta);
		boardDetails.setLikeCount(boardDetails.getLikeCount() + likeCountDelta);
		boardDetails.setViewCount(boardDetails.getViewCount() + viewCountDelta);
	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoards(int categoryCode, BoardCriteria boardCri, Long userId) {
		log.info("\t > find pagedBoards (categoryCode:{}, keywordRegexp:{})", categoryCode, boardCri.getKeywordRegexp());
		return boardMapper.findPagedBoards(categoryCode, boardCri, userId)
				.stream()
				.map(boardDetailsMap -> BoardDetailsDto.from(boardDetailsMap))
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getPagination(int categoryCode, BoardCriteria boardCri) {
		int totalCnt = boardMapper.findTotalCntForPaging(categoryCode, boardCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, boardCri.getPage(), boardCri.getRecordCnt(), pageRange) : null;
	}
	
	@Override
	public BoardDto getModifyBoard(Long boardId, Long userId) {
		Board board = getBoard(boardId);
		if (!board.getUserId().equals(userId)) {
			throw new ExpectedException("error.board.notWriter");
		}
		
		return BoardDto.from(board);
	}
	
	@Override
	public Board modifyBoard(BoardDto boardDto) {
		Board board = getBoard(boardDto.getId());
		if (board.getUserId() != boardDto.getUserId()) {
			throw new ExpectedException("error.board.notWriter");
		}
		
		log.info("\t > produce searchContent by parsing content for search");
		String searchContent = Jsoup.parse(boardDto.getContent()).text();
		boardDto.setSearchContent(searchContent);
		
		Board modifiyBoard = boardDto.toEntity();
		boolean isUpdated = boardMapper.updateBoard(modifiyBoard);
		if (!isUpdated) {
			throw new ExpectedException("error.board.update");
		}
		
		imageService.updateBoardImages(boardDto.getImages(), modifiyBoard.getId());
		
		return modifiyBoard;
	}
	
	@Override
	public Board deleteBoard(Long boardId, Long userId) {
		Board board = getBoard(boardId);
		if (!board.getUserId().equals(userId)) {
			throw new ExpectedException("error.board.notWriter");
		}
		
		boolean isDeleted = boardMapper.deleteBoard(board);
		if (!isDeleted) {
			throw new ExpectedException("error.board.delete");
		}
		
		return board;
	}
	
	@Override
	public Board getBoard(Long boardId) {
		Board board = boardMapper.findBoardById(boardId);
		if (board == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		return board;
	}

	@Deprecated
	@Override
	public void updateCommentCount(int count, Long boardId) {
		log.info("\t > update commentCount");
		boardMapper.updateCommentCount(count, boardId);
	}

	@Deprecated
	@Override
	public void increaseCommentCount(Long boardId) {
		log.info("\t > increase commentCount");
		boardMapper.increaseCommentCount(boardId);
	}

	@Deprecated
	@Override
	public void decreaseCommentCount(Long boardId) {
		log.info("\t > decrease commentCount");
		boardMapper.decreaseCommentCount(boardId);
	}
	
	@Override
	public void applyCommentCountDelta(Integer countDelta, Long boardId) {
		log.info("\t > apply commentCount delta");
		boardMapper.applyCommentCountDelta(countDelta, boardId);
	}

	@Override
	public void applyLikeCountDelta(Integer countDelta, Long boardId) {
		log.info("\t > apply likeCount delta");
		boardMapper.applyLikeCountDelta(countDelta, boardId);
	}
	
	@Override
	public void applyViewCountDelta(Integer countDelta, Long boardId) {
		log.info("\t > apply viewCount delta");
		boardMapper.applyViewCountDelta(countDelta, boardId);
	}
	
}
