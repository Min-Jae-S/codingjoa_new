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
		log.info("\t > convert boardDto to board entity");
		log.info("\t > produce boardContentText by parsing boardContent for search");

		board.setBoardContentText(boardContentText);
		log.info("\t > {}", board);
		
		boardMapper.insertBoard(board);
		Integer boardIdx = board.getBoardIdx();
		log.info("\t > after inserting board, boardIdx = {}", boardIdx);
		
		if (boardIdx == null) {
			throw new ExpectedException("error.WriteBoard");
		}

		boardDto.setBoardIdx(boardIdx);
		imageService.activateBoardImages(boardDto);
	}

	@Override
	public BoardDetailsDto getBoardDetails(int boardIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetails(boardIdx);
		log.info("\t > find boardDetailsMap = {}", boardDetailsMap);
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		log.info("\t > convert boardDetailsMap to BoardDetailsDto");
		return modelMapper.map(boardDetailsMap, BoardDetailsDto.class);
	}
	
	@Override
	public void updateBoardViews(int boardIdx) {
		log.info("\t > update board views");
		boardMapper.updateBoardViews(boardIdx);
	}
	
	@Override
	public Criteria createNewBoardCri(Criteria boardCri) {
		Criteria newBoardCri = new Criteria(boardCri);
		if (!"writer".equals(boardCri.getType())) {
			return newBoardCri;
		}
		
		// keyword is always not null
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
		Board modifyBoard = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > find modifyBoard = {}", modifyBoard);

		if (modifyBoard == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Integer DBboardWriterIdx = modifyBoard.getBoardWriterIdx();
		log.info("\t > DB boardWriterIdx = {}, My boardWriterIdx = {}", DBboardWriterIdx, boardWriterIdx);
		
		if (DBboardWriterIdx != boardWriterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		return modelMapper.map(modifyBoard, BoardDto.class);
	}
	
	@Override
	public void modifyBoard(BoardDto boardDto) {
		Board modifyBoard = boardMapper.findBoardByIdx(boardDto.getBoardIdx());
		log.info("\t > find modifyBoard = {}", modifyBoard);

		if (modifyBoard == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Integer DBboardWriterIdx = modifyBoard.getBoardWriterIdx();
		int boardWirterIdx = boardDto.getBoardWriterIdx();
		log.info("\t > DB boardWriterIdx = {}, My boardWriterIdx = {}", DBboardWriterIdx, boardWirterIdx);
		
		if (DBboardWriterIdx != boardWirterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		String newBoardContent = boardDto.getBoardContent();
		String newBoardContentText = Jsoup.parse(newBoardContent).text();
		log.info("\t > produce boardContentText by parsing boardContent for search");
		
		modifyBoard.setBoardContent(newBoardContent);
		modifyBoard.setBoardContentText(newBoardContentText);
		modifyBoard.setBoardTitle(boardDto.getBoardTitle());
		modifyBoard.setBoardCategoryCode(boardDto.getBoardCategoryCode());
		log.info("\t > new modifyBoard = {}", modifyBoard);

		boardMapper.updateBoard(modifyBoard);
		imageService.modifyBoardImages(boardDto);
	}
	
//	@Override
//	public void modifyBoard(BoardDto boardDto) {
//		Board board = modelMapper.map(boardDto, Board.class);
//		String boardContentText = Jsoup.parse(board.getBoardContent()).text();
//		board.setBoardContentText(boardContentText);
//		log.info("\t > convert boardDto to board entity");
//		log.info("\t > produce boardContentText by parsing boardContent for search");
//		log.info("\t > {}", board);
//
//		boardMapper.updateBoard(board);
//		Integer DBboardWriterIdx = board.getBoardWriterIdx();
//		log.info("\t > after updating board, DB boardWriterIdx = {}, My boardWriterIdx = {}", 
//				DBboardWriterIdx, boardDto.getBoardWriterIdx());
//		
//		if (DBboardWriterIdx == null) {
//			throw new ExpectedException("error.UpdateBoard");
//		}
//		
//		if (DBboardWriterIdx != boardDto.getBoardWriterIdx()) {
//			throw new ExpectedException("error.NotMyBoard");
//		}
//		
//		imageService.modifyBoardImages(boardDto);
//	}

	@Override
	public int getBoardCategoryCode(int boardIdx) {
		return boardMapper.findBoardCategoryCode(boardIdx);
	}

	@Override
	public void deleteBoard(BoardDto boardDto) {
		Board deleteBoard = boardMapper.findBoardByIdx(boardDto.getBoardIdx());
		log.info("\t > find deleteBoard = {}", deleteBoard);

		if (deleteBoard == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		Integer DBboardWriterIdx = deleteBoard.getBoardWriterIdx();
		int boardWirterIdx = boardDto.getBoardWriterIdx();
		log.info("\t > DB boardWriterIdx = {}, My boardWriterIdx = {}", DBboardWriterIdx, boardWirterIdx);
		
		if (DBboardWriterIdx != boardWirterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		boardMapper.deleteBoard(deleteBoard.getBoardIdx());
		boardDto.setBoardCategoryCode(deleteBoard.getBoardCategoryCode());
	}

//	@Override
//	public void deleteBoard(BoardDto boardDto) {
//		Board board = modelMapper.map(boardDto, Board.class);
//		log.info("\t > convert boardDto to board entity");
//		log.info("\t > {}", board);
//		
//		boardMapper.deleteBoard(board);
//		Integer DBboardWriterIdx = board.getBoardWriterIdx();
//		log.info("\t > after deleting board, DB boardWriterIdx = {}, My boardWriterIdx = {}", 
//				DBboardWriterIdx, boardDto.getBoardWriterIdx());
//		
//		if (DBboardWriterIdx == null) {
//			throw new ExpectedException("error.DeleteBoard");
//		}
//		
//		if (DBboardWriterIdx != boardDto.getBoardWriterIdx()) {
//			throw new ExpectedException("error.NotMyBoard");
//		}
//		
//		boardDto.setBoardCategoryCode(board.getBoardCategoryCode());
//	}
}
