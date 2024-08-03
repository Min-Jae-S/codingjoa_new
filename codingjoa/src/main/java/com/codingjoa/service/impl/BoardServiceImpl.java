package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@PropertySource("/WEB-INF/properties/pagination.properties")
@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	private final BoardMapper boardMapper;
	private final ModelMapper modelMapper;
	private final ImageService imageService;
	private final int pageRange;
	
	@Autowired
	public BoardServiceImpl(BoardMapper boardMapper, ModelMapper modelMapper, ImageService imageService,
			@Value("${pagination.pageRange}") int pageRange) {
		this.boardMapper = boardMapper;
		this.modelMapper = modelMapper;
		this.imageService = imageService;
		this.pageRange = pageRange;
	}

	@Override
	public void writeBoard(BoardDto boardDto) {
		String boardContentText = Jsoup.parse(boardDto.getBoardContent()).text();
		log.info("\t > produce boardContentText by parsing boardContent for search");

		Board board = modelMapper.map(boardDto, Board.class);
		board.setBoardContentText(boardContentText);
		log.info("\t > convert boardDto to board entity");
		log.info("\t > board = {}", board);
		
		boardMapper.insertBoard(board);
		Integer dbBoardIdx = board.getBoardIdx();
		log.info("\t > after inserting board, boardIdx = {}", dbBoardIdx);
		
		if (dbBoardIdx == null) {
			throw new ExpectedException("error.WriteBoard");
		}

		boardDto.setBoardIdx(dbBoardIdx);
		imageService.activateBoardImages(boardDto);
	}

	@Override
	public BoardDetailsDto getBoardDetails(int boardIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetailsByIdx(boardIdx);
		log.info("\t > find boardDetailsMap");
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return modelMapper.map(boardDetailsMap, BoardDetailsDto.class);
	}
	
	@Override
	public void updateBoardViews(int boardIdx) {
		log.info("\t > update board views");
		boardMapper.updateBoardViews(boardIdx);
	}
	
//	@Override
//	public Criteria createNewBoardCri(Criteria boardCri) {
//		log.info("\t > create newBoardCri");
//		Criteria newBoardCri = new Criteria(boardCri);
//		if (!"writer".equals(boardCri.getType())) {
//			return newBoardCri;
//		}
//		
//		String keyword = boardCri.getKeyword();
//		if ("".equals(keyword)) {
//			return newBoardCri;
//		}
//		
//		log.info("\t > produce newKeyword by finding memberIdx");
//		String newKeyword = boardMapper.findMemberIdxByKeyword(keyword)
//				.stream()
//				.map(memberIdx -> memberIdx.toString())
//				.collect(Collectors.joining("_"));
//
//		if (!"".equals(keyword)) {
//			newBoardCri.setKeyword(newKeyword);
//		}
//		return newBoardCri;
//	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri) {
		log.info("\t > find pagedBoard");
		log.info("\t > boardCri = {}, keywordRegexp = {}", boardCri, boardCri.getKeywordRegexp());
		return boardMapper.findPagedBoard(boardCategoryCode, boardCri)
				.stream()
				.map(boardDetailsMap -> modelMapper.map(boardDetailsMap, BoardDetailsDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getPagination(int boardCategoryCode, Criteria boardCri) {
		int totalCnt = boardMapper.findPagedBoardTotalCnt(boardCategoryCode, boardCri);
		return new Pagination(totalCnt, boardCri.getPage(), boardCri.getRecordCnt(), pageRange);
	}
	
	@Override
	public BoardDto getModifyBoard(int boardIdx, int boardWriterIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > find board");

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Integer dbBoardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > dbBoardWriterIdx = {}, boardWriterIdx = {}", dbBoardWriterIdx, boardWriterIdx);
		
		if (dbBoardWriterIdx != boardWriterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		return modelMapper.map(board, BoardDto.class);
	}
	
	@Override
	public void modifyBoard(BoardDto boardDto) {
		Board board = boardMapper.findBoardByIdx(boardDto.getBoardIdx());
		log.info("\t > find board");

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Integer dbBoardWriterIdx = board.getBoardWriterIdx();
		int boardWirterIdx = boardDto.getBoardWriterIdx();
		log.info("\t > dbBoardWriterIdx = {}, boardWriterIdx = {}", dbBoardWriterIdx, boardWirterIdx);
		
		if (dbBoardWriterIdx != boardWirterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		String newBoardContent = boardDto.getBoardContent();
		String newBoardContentText = Jsoup.parse(newBoardContent).text();
		log.info("\t > produce boardContentText by parsing boardContent for search");

		board.setBoardTitle(boardDto.getBoardTitle());
		board.setBoardContent(newBoardContent);
		board.setBoardContentText(newBoardContentText);
		board.setBoardCategoryCode(boardDto.getBoardCategoryCode());
		log.info("\t > set up modifyBoard using the found board");
		
		boardMapper.updateBoard(board);
		imageService.modifyBoardImages(boardDto);
	}
	
	@Override
	public int getBoardCategoryCode(int boardIdx) {
		return boardMapper.findBoardCategoryCodeByIdx(boardIdx);
	}

	@Override
	public int deleteBoard(int boardIdx, int boardWriterIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > find board");

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Integer dbBoardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > dbBoardWriterIdx = {}, boardWriterIdx = {}", dbBoardWriterIdx, boardWriterIdx);
		
		if (dbBoardWriterIdx != boardWriterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		boardMapper.deleteBoard(board);
		return board.getBoardCategoryCode();
	}
	
}
