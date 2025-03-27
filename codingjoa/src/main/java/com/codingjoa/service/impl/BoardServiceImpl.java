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
		log.info("\t > convert boardDto to board entity = {}", board);
		
		boolean isBoardSaved = boardMapper.insertBoard(board);
		log.info("\t > saved board = {}", (board != null) ? board.getId() : board);

		if (!isBoardSaved) {
			throw new ExpectedException("error.board.save");
		}
		
		imageService.activateBoardImages(boardDto.getImages(), board.getId());
		
		return board;
	}

	@Override
	public BoardDetailsDto getBoardDetails(Long boardId, Long userId) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetailsById(boardId, userId);
		log.info("\t > found boardDetailsMap = {}", boardDetailsMap);
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		return BoardDetailsDto.from(boardDetailsMap);
	}
	
	@Override
	public void increaseViewCount(Long boardId) {
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
	public BoardDto getModifyBoard(Long boardId, Long userId) {
		Board board = boardMapper.findBoardById(boardId);
		log.info("\t > found board = {}", board);
		
		if (board == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		if (board.getUserId() != userId) {
			throw new ExpectedException("error.board.notWriter");
		}
		
		return BoardDto.from(board);
	}
	
	@Override
	public Board modifyBoard(BoardDto boardDto) {
		Board board = boardMapper.findBoardById(boardDto.getId());
		log.info("\t > found board = {}", board);

		if (board == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		if (board.getUserId() != boardDto.getUserId()) {
			throw new ExpectedException("error.board.notWriter");
		}
		
		log.info("\t > produce searchContent by parsing content for search");
		String searchContent = Jsoup.parse(boardDto.getContent()).text();
		boardDto.setSearchContent(searchContent);
		
		Board modifiyBoard = boardDto.toEntity();
		log.info("\t > convert boardDto to board entity = {}", modifiyBoard);
		
		boolean isUpdated = boardMapper.updateBoard(modifiyBoard);
		if (!isUpdated) {
			throw new ExpectedException("error.board.update");
		}
		
		imageService.updateBoardImages(boardDto.getImages(), modifiyBoard.getId());
		
		return modifiyBoard;
	}
	
	@Override
	public Board deleteBoard(Long boardId, Long userId) {
		Board board = boardMapper.findBoardById(boardId);
		log.info("\t > found board = {}", board);

		if (board == null) {
			throw new ExpectedException("error.board.notFound");
		}
		
		if (board.getUserId() != userId) {
			throw new ExpectedException("error.board.notWriter");
		}
		
		boolean isDeleted = boardMapper.deleteBoard(board);
		if (!isDeleted) {
			throw new ExpectedException("error.board.delete");
		}
		
		return board;
	}
	
}
