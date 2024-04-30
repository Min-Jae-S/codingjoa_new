package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("/WEB-INF/properties/pagination.properties")
@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ImageService imageService;
	
	@Value("${pagination.pageRange}")
	private int pageRange;
	
	@Override
	public void writeBoard(BoardDto boardDto) {
		Board board = modelMapper.map(boardDto, Board.class);
		String boardContentText = Jsoup.parse(board.getBoardContent()).text();
		log.info("\t > produce boardContentText by parsing boardContent for search");
		
		board.setBoardContentText(boardContentText);
		log.info("\t > insert {}", board);
		boardMapper.insertBoard(board);
		
		Integer boardIdx = board.getBoardIdx();
		log.info("\t > registerd boardIdx = {}", boardIdx);
		
		if (boardIdx == null) {
			throw new ExpectedException("error.WriteBoard");
		}

		boardDto.setBoardIdx(boardIdx);
		imageService.activateBoardImage(boardDto);
	}

	@Override
	public BoardDetailsDto getBoardDetails(int boardIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetails(boardIdx);
		log.info("\t > find boardDetailsMap = {}", boardDetailsMap);
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return modelMapper.map(boardDetailsMap, BoardDetailsDto.class);
	}
	
	@Override
	public void updateBoardViews(int boardIdx) {
		boardMapper.updateBoardViews(boardIdx);
	}
	
	@Override
	public Criteria makeNewBoardCri(Criteria boardCri) {
		Criteria newBoardCri = new Criteria(boardCri);
		if (!"writer".equals(boardCri.getType())) {
			return newBoardCri;
		}
		
		// keyword always not null
		String keyword = boardCri.getKeyword(); 
		if (!"".equals(keyword)) {
			String newKeyword = boardMapper.findMemberIdxByKeyword(keyword)
					.stream()
					.map(memberIdx -> memberIdx.toString())
					.collect(Collectors.joining("_"));
			newBoardCri.setKeyword(newKeyword);
		}
		
		return newBoardCri;
	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri) {
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
		log.info("\t > find modifyBoard = {}", board);

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Integer DBboardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > current boardWriterIdx = {}, DBboardWriterIdx = {}", boardWriterIdx, DBboardWriterIdx);
		
		if (DBboardWriterIdx != boardWriterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		return modelMapper.map(board, BoardDto.class);
	}
	
	@Override
	public void modifyBoard(BoardDto boardDto) {
		Board board = modelMapper.map(boardDto, Board.class);
		String boardContentText = Jsoup.parse(board.getBoardContent()).text();
		log.info("\t > produce boardContentText by parsing boardContent for search");

		board.setBoardContentText(boardContentText);
		log.info("\t > update {}", board);
		boardMapper.updateBoard(board);
		
		Integer DBboardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > current boardWriterIdx = {}, DBboardWriterIdx = {}", boardDto.getBoardWriterIdx(), DBboardWriterIdx);
		
		if (DBboardWriterIdx == null) {
			throw new ExpectedException("error.UpdateBoard");
		}
		
		if (DBboardWriterIdx != boardDto.getBoardWriterIdx()) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		imageService.deactivateBoardImage(boardDto);
		imageService.activateBoardImage(boardDto);
	}

	@Override
	public int getBoardCategoryCode(int boardIdx) {
		return boardMapper.findBoardCategoryCode(boardIdx);
	}

	@Override
	public void deleteBoard(BoardDto boardDto) {
		Board board = modelMapper.map(boardDto, Board.class);
		log.info("\t > delete {}", board);
		boardMapper.deleteBoard(board);
		
		Integer DBboardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > current boardWriterIdx = {}, DBboardWriterIdx = {}", boardDto.getBoardWriterIdx(), DBboardWriterIdx);
		
		if (DBboardWriterIdx == null) {
			throw new ExpectedException("error.DeleteBoard");
		}
		
		if (DBboardWriterIdx != boardDto.getBoardWriterIdx()) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		boardDto.setBoardCategoryCode(board.getBoardCategoryCode());
	}
}
