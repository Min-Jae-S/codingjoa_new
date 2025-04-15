package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	private final BoardMapper boardMapper;
	private final ImageService imageService;
	private final int pageRange;
	
	public BoardServiceImpl(BoardMapper boardMapper, ImageService imageService,
			@Value("${pagination.pageRange}") int pageRange) {
		this.boardMapper = boardMapper;
		this.imageService = imageService;
		this.pageRange = pageRange;
	}

	@Override
	public Board saveBoard(BoardDto boardDto) {
		log.info("\t > produce searchContent by parsing content for search");
		String searchContent = Jsoup.parse(boardDto.getContent()).text();
		boardDto.setSearchContent(searchContent);

		Board board = boardDto.toEntity();
		boolean isSaved = boardMapper.insertBoard(board);
		log.info("\t > saved board = {}", board.getId());

		if (!isSaved) {
			throw new ExpectedException("error.board.save");
		}
		
		imageService.activateBoardImages(boardDto.getImages(), board.getId());
		
		return board;
	}

	@Override
	public BoardDetailsDto getBoardDetails(long boardId, Long userId) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetailsById(boardId, userId);
		log.info("\t > found boardDetailsMap = {}", boardDetailsMap);
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		return BoardDetailsDto.from(boardDetailsMap);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void increaseViewCount(long boardId) {
		log.info("\t > increase view count");
		boardMapper.increaseViewCount(boardId);
	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoards(int categoryCode, BoardCriteria boardCri, Long userId) {
		log.info("\t > find pagedBoards (categoryCode = {}, keywordRegexp = {})", categoryCode, boardCri.getKeywordRegexp());
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
	public BoardDto getModifyBoard(long boardId, long userId) {
		Board board = getBoard(boardId);
		
		if (board.getUserId() != userId) {
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
	public Board deleteBoard(long boardId, long userId) {
		Board board = getBoard(boardId);
		
		if (board.getUserId() != userId) {
			throw new ExpectedException("error.board.notWriter");
		}
		
		boolean isDeleted = boardMapper.deleteBoard(board);
		if (!isDeleted) {
			throw new ExpectedException("error.board.delete");
		}
		
		return board;
	}
	
	@Override
	public Board getBoard(long boardId) {
		Board board = boardMapper.findBoardById(boardId);
		log.info("\t > found board = {}", board);
		
		if (board == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		return board;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void increaseCommentCount(long boardId) {
		log.info("\t > increase comment count");
		boardMapper.increaseCommentCount(boardId);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void decreaseCommentCount(long boardId) {
		log.info("\t > decrease comment count");
		boardMapper.decreaseCommentCount(boardId);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void increaseLikeCount(long boardId) {
		log.info("\t > increase like count");
		boardMapper.increaseLikeCount(boardId);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void decreaseLikeCount(long boardId) {
		log.info("\t > decrease like count");
		boardMapper.decreaseLikeCount(boardId);
	}
	
}
