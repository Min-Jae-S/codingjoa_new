package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.exception.ExpectedException;
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
	
	@Autowired
	public BoardServiceImpl(BoardMapper boardMapper, ImageService imageService,
			@Value("${pagination.pageRange}") int pageRange) {
		this.boardMapper = boardMapper;
		this.imageService = imageService;
		this.pageRange = pageRange;
	}

	@Override
	public Board saveBoard(BoardDto boardDto) {
		log.info("\t > produce boardContentText by parsing boardContent for search");
		String boardContentText = Jsoup.parse(boardDto.getBoardContent()).text();
		boardDto.setBoardContentText(boardContentText);

		Board board = boardDto.toEntity();
		log.info("\t > convert boardDto to board entity = {}", board);
		
		boolean isBoardSaved = boardMapper.insertBoard(board);
		if (!isBoardSaved) {
			throw new ExpectedException("error.SaveBoard");
		}
		
		imageService.activateBoardImages(boardDto.getBoardImages(), board.getBoardIdx());
		
		return board;
	}

	@Override
	public BoardDetailsDto getBoardDetails(int boardIdx, Integer memberIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetailsByIdx(boardIdx, memberIdx);
		log.info("\t > find boardDetailsMap = {}", boardDetailsMap);
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return BoardDetailsDto.from(boardDetailsMap);
	}
	
	@Override
	public void updateBoardViews(int boardIdx) {
		log.info("\t > update boardViews");
		boardMapper.updateBoardViews(boardIdx);
	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, BoardCriteria boardCri, Integer memberIdx) {
		log.info("\t > find pagedBoard");
		return boardMapper.findPagedBoard(boardCategoryCode, boardCri, memberIdx)
				.stream()
				.map(boardDetailsMap -> {
					BoardDetailsDto boardDetails = BoardDetailsDto.from(boardDetailsMap);
					log.info("\t\t - {}", boardDetails.getInfo());
					return boardDetails;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getPagination(int boardCategoryCode, BoardCriteria boardCri) {
		int totalCnt = boardMapper.findPagedBoardTotalCnt(boardCategoryCode, boardCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, boardCri.getPage(), boardCri.getRecordCnt(), pageRange) : null;
	}
	
	private Board getBoardByIdx(int boardIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > find board = {}", board);

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return board;
	}
	
	@Override
	public BoardDto getModifyBoard(int boardIdx, int memberIdx) {
		Board board = getBoardByIdx(boardIdx);
		if (board.getBoardWriterIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		return BoardDto.from(board);
	}
	
	@Override
	public Board updateBoard(BoardDto boardDto) {
		Board board = getBoardByIdx(boardDto.getBoardIdx());
		if (board.getBoardWriterIdx() != boardDto.getBoardWriterIdx()) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		log.info("\t > produce boardContentText by parsing boardContent for search");
		String boardContentText = Jsoup.parse(boardDto.getBoardContent()).text();
		boardDto.setBoardContentText(boardContentText);
		
		Board modifiyBoard = boardDto.toEntity();
		log.info("\t > convert boardDto to board entity = {}", modifiyBoard);
		
		boolean isUpdated = boardMapper.updateBoard(modifiyBoard);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateBoard");
		}
		
		imageService.modifyBoardImages(boardDto.getBoardImages(), modifiyBoard.getBoardIdx());
		
		return modifiyBoard;
	}
	
	@Override
	public int getBoardCategoryCode(int boardIdx) {
		return boardMapper.findBoardCategoryCodeByIdx(boardIdx);
	}

	@Override
	public Board deleteBoard(int boardIdx, int memberIdx) {
		Board board = getBoardByIdx(boardIdx);
		if (board.getBoardWriterIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		boolean isDeleted = boardMapper.deleteBoard(board);
		if (!isDeleted) {
			throw new ExpectedException("error.DeleteBoard");
		}
		
		return board;
	}
	
}
