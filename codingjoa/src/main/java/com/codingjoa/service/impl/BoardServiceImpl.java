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
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.ImageService;
import com.codingjoa.util.FormatUtils;

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
	public Integer saveBoard(BoardDto boardDto) {
		log.info("\t > produce boardContentText by parsing boardContent for search");
		String boardContentText = Jsoup.parse(boardDto.getBoardContent()).text();
		boardDto.setBoardContentText(boardContentText);

		Board board = boardDto.toEntity();
		log.info("\t > convert boardDto to board entity = {}", board);
		
		boolean isBoardSaved = boardMapper.insertBoard(board);
		if (!isBoardSaved) {
			throw new ExpectedException("error.SaveBoard");
		}
		
		Integer boardIdx = board.getBoardIdx();
		imageService.activateBoardImages(boardDto.getBoardImages(), boardIdx);
		
		return boardIdx;
	}

	@Override
	public BoardDetailsDto getBoardDetails(int boardIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetailsByIdx(boardIdx);
		log.info("\t > find boardDetailsMap = {}", FormatUtils.formatFields(boardDetailsMap));
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return BoardDetailsDto.from(boardDetailsMap);
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
				.map(boardDetailsMap -> BoardDetailsDto.from(boardDetailsMap))
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getPagination(int boardCategoryCode, Criteria boardCri) {
		int totalCnt = boardMapper.findPagedBoardTotalCnt(boardCategoryCode, boardCri);
		return new Pagination(totalCnt, boardCri.getPage(), boardCri.getRecordCnt(), pageRange);
	}
	
	@Override
	public BoardDto getModifyBoard(int boardIdx, int memberIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > find board = {}", board);

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		if (board.getMemberIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		return BoardDto.from(board);
	}
	
	@Override
	public void updateBoard(BoardDto boardDto) {
		Board board = boardMapper.findBoardByIdx(boardDto.getBoardIdx());
		log.info("\t > find board = {}", board);

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		if (board.getMemberIdx() !=  boardDto.getMemberIdx()) {
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
		
		Integer dbBoardWriterIdx = board.getMemberIdx();
		log.info("\t > dbBoardWriterIdx = {}, boardWriterIdx = {}", dbBoardWriterIdx, boardWriterIdx);
		
		if (dbBoardWriterIdx != boardWriterIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		boardMapper.deleteBoard(board);
		return board.getBoardCategoryCode();
	}
	
}
