package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.UploadService;
import com.codingjoa.util.MessageUtils;

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
	private UploadService uploadService;
	
	@Value("${pagination.pageRange}")
	private int pageRange;
	
	@Override
	public void writeBoard(BoardDto boardDto) {
		Board board = modelMapper.map(boardDto, Board.class);
		log.info("\t > writeBoardDto ==> {}", board);
		
		boardMapper.insertBoard(board);
		Integer DBboardIdx = board.getBoardIdx();
		log.info("\t > DB boardIdx = {}", DBboardIdx);
		
		if (DBboardIdx == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.WriteBoard"));
		}

		boardDto.setBoardIdx(DBboardIdx);
		uploadService.activateImage(boardDto);
	}

	@Override
	public BoardDetailsDto getBoardDetails(int boardIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetails(boardIdx);
		if (boardDetailsMap == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundBoard"));
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
		
		String keyword = boardCri.getKeyword();
		if (StringUtils.hasText(keyword)) {
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
	public boolean isBoardIdxExist(int boardIdx, int boardCategoryCode) {
		return boardMapper.isBoardIdxExist(boardIdx, boardCategoryCode);
	}
	
	@Override
	public BoardDto getModifyBoard(int boardIdx, int boardWriterIdx) {
		Board board = boardMapper.findModifyBoard(boardIdx);
		log.info("\t > find modifyBoard, {}", board);

		if (board == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundModifyBoard"));
		}
		
		Integer DBboardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > my boardWriterIdx = {}", boardWriterIdx);
		log.info("\t > DB boardWriterIdx = {}", DBboardWriterIdx);
		
		if (DBboardWriterIdx != boardWriterIdx) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotMyBoard"));
		}
		
		return modelMapper.map(board, BoardDto.class);
	}
	
	@Override
	public void modifyBoard(BoardDto boardDto) {
		Board board = modelMapper.map(boardDto, Board.class);
		log.info("\t > modifyBoardDto ==> {}", board);

		boardMapper.updateBoard(board);
		Integer DBboardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > my boardWriterIdx = {}", boardDto.getBoardWriterIdx());
		log.info("\t > DB boardWriterIdx = {}", DBboardWriterIdx);
		
		if (DBboardWriterIdx == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.UpdateBoard"));
		}
		
		if (DBboardWriterIdx != boardDto.getBoardWriterIdx()) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotMyBoard"));
		}
		
		uploadService.deactivateImage(boardDto);
		uploadService.activateImage(boardDto);
	}

	@Override
	public int getBoardCategoryCode(int boardIdx) {
		return boardMapper.findBoardCategoryCode(boardIdx);
	}

	@Override
	public void deleteBoard(BoardDto boardDto) {
		Board board = modelMapper.map(boardDto, Board.class);
		log.info("\t > deleteBoardDto ==> {}", board);
		
		boardMapper.deleteBoard(board);
		Integer DBboardWriterIdx = board.getBoardWriterIdx();
		log.info("\t > my boardWriterIdx = {}", boardDto.getBoardWriterIdx());
		log.info("\t > DB boardWriterIdx = {}, DB boardCategoryCode = {}", DBboardWriterIdx, board.getBoardCategoryCode());
		
		if (DBboardWriterIdx == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.DeleteBoard"));
		}
		
		if (DBboardWriterIdx != boardDto.getBoardWriterIdx()) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotMyBoard"));
		}
		
		boardDto.setBoardCategoryCode(board.getBoardCategoryCode());
	}
}
