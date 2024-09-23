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
	public BoardDetailsDto getBoardDetails(Integer boardIdx, Integer memberIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetails(boardIdx, memberIdx);
		log.info("\t > find boardDetailsMap = {}", boardDetailsMap);
		
		if (boardDetailsMap == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return BoardDetailsDto.from(boardDetailsMap);
	}
	
	@Override
	public void updateBoardViews(Integer boardIdx) {
		log.info("\t > update boardViews");
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
	public List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, Criteria boardCri, Integer memberIdx) {
		log.info("\t > find pagedBoard");
		return boardMapper.findPagedBoard(boardCategoryCode, boardCri, memberIdx)
				.stream()
				.map(boardDetailsMap -> BoardDetailsDto.from(boardDetailsMap))
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getPagination(int boardCategoryCode, Criteria boardCri) {
		int totalCnt = boardMapper.findPagedBoardTotalCnt(boardCategoryCode, boardCri);
		return new Pagination(totalCnt, boardCri.getPage(), boardCri.getRecordCnt(), pageRange);
	}
	
	private Board getBoardByIdx(Integer boardIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > find board = {}", board);

		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return board;
	}
	
	@Override
	public BoardDto getModifyBoard(Integer boardIdx, Integer memberIdx) {
		Board board = getBoardByIdx(boardIdx);
		if (board.getMemberIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		return BoardDto.from(board);
	}
	
	@Override
	public Integer updateBoard(BoardDto boardDto) {
		Board board = getBoardByIdx(boardDto.getBoardIdx());
		if (board.getMemberIdx() != boardDto.getMemberIdx()) {
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
		
		Integer boardIdx = board.getBoardIdx();
		imageService.modifyBoardImages(boardDto.getBoardImages(), boardIdx);
		
		return boardIdx;
	}
	
	@Override
	public int getBoardCategoryCode(Integer boardIdx) {
		return boardMapper.findBoardCategoryCodeByIdx(boardIdx);
	}

	@Override
	public int deleteBoard(Integer boardIdx, Integer memberIdx) {
		Board board = getBoardByIdx(boardIdx);
		if (board.getMemberIdx() != memberIdx) {
			throw new ExpectedException("error.NotMyBoard");
		}
		
		boolean isDeleted = boardMapper.deleteBoard(board);
		if (!isDeleted) {
			throw new ExpectedException("error.DeleteBoard");
		}
		
		return board.getBoardCategoryCode();
	}
	
	
}
