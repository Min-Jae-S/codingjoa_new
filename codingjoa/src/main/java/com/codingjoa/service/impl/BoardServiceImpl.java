package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.entity.Upload;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.mapper.UploadMapper;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@PropertySource("/WEB-INF/properties/pagination.properties")
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private UploadMapper uploadMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${pagination.pageRange}")
	private int pageRange;
	
	@Override
	public int uploadImage(String uploadFilename) {
		Upload upload = new Upload();
		upload.setUploadFile(uploadFilename);
		
		uploadMapper.insertUpload(upload);
		
		return upload.getUploadIdx();
	}

	@Override
	public int writeBoard(BoardDto writeBoardDto) {
		Board board = modelMapper.map(writeBoardDto, Board.class);
		log.info("writeBoardDto ==> {}", board);
		
		boardMapper.insertBoard(board);
		
		return board.getBoardIdx();
	}

	@Override
	public boolean isImageUploaded(int uploadIdx) {
		return uploadMapper.isImageUploaded(uploadIdx);
	}

	@Override
	public void activateImage(BoardDto writeBoardDto) {
		List<Integer> uploadIdxList = writeBoardDto.getUploadIdxList();
		
		if (uploadIdxList != null) {
			uploadMapper.activateImage(writeBoardDto.getBoardIdx(), uploadIdxList);
		}
	}

	@Override
	public BoardDetailsDto getBoardDetails(int boardIdx) {
		Map<String, Object> boardDetailsMap = boardMapper.findBoardDetails(boardIdx);
		log.info("boardMapper.findBoardDetails(boardIdx) = {}", boardDetailsMap);
		
		return modelMapper.map(boardDetailsMap, BoardDetailsDto.class);
	}
	
	@Override
	public void updateBoardViews(int boardIdx) {
		boardMapper.updateBoardViews(boardIdx);
	}
	
	@Override
	public BoardCriteria makeNewBoardCri(BoardCriteria boardCri) {
		BoardCriteria newCri = new BoardCriteria(boardCri);
		String keyword = boardCri.getKeyword();
		
		if (!"writer".equals(boardCri.getType())) {
			log.info("Keyword Regexp = {}", newCri.getKeywordRegexp());
			return newCri;
		}
		
		if (StringUtils.hasText(keyword)) {
			String newKeyword = boardMapper.findMemberIdxByKeyword(keyword).stream()
					.map(memberIdx -> memberIdx.toString())
					.collect(Collectors.joining("_"));
			log.info("New Keyword = {}", newKeyword);
			
			newCri.setKeyword(newKeyword);
		}
		
		return newCri;
	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoard(int boardCategoryCode, BoardCriteria boardCri) {
		log.info("boardMapper.findPagedBoard(boardCategoryCode, boardCri) = {}", 
				boardMapper.findPagedBoard(boardCategoryCode, boardCri));
		
		return boardMapper.findPagedBoard(boardCategoryCode, boardCri).stream()
				.map(boardDetailsMap -> modelMapper.map(boardDetailsMap, BoardDetailsDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getPagination(int boardCategoryCode, BoardCriteria boardCri) {
		int totalCnt = boardMapper.findPagedBoardTotalCnt(boardCategoryCode, boardCri);
		return new Pagination(totalCnt, boardCri.getPage(), boardCri.getRecordCnt(), pageRange);
	}
	
	@Override
	public boolean isBoardIdxExist(int boardIdx, int boardCategoryCode) {
		return boardMapper.isBoardIdxExist(boardIdx, boardCategoryCode);
	}

	@Override
	public void bindModifyBoard(BoardDto modifyBoardDto) {
		int boardIdx = modifyBoardDto.getBoardIdx();
		
		Board board = boardMapper.findBoardByIdx(boardIdx);
		modelMapper.map(board, modifyBoardDto);
		
		List<Integer> uploadIdxList = uploadMapper.findUploadIdxList(boardIdx);
		modifyBoardDto.setUploadIdxList(uploadIdxList);
	}
	
	@Override
	public boolean isMyBoard(int boardIdx, int boardWriterIdx) {
		return boardMapper.isMyBoard(boardIdx, boardWriterIdx);
	}

	@Override
	public void modifyBoard(BoardDto modifyBoardDto) {
		Board board = modelMapper.map(modifyBoardDto, Board.class);
		log.info("modifyBoardDto ==> {}", board);
		
		boardMapper.updateBoard(board);
	}

	@Override
	public void modifyUpload(BoardDto modifyBoardDto) {
		int boardIdx = modifyBoardDto.getBoardIdx();
		uploadMapper.deactivateImage(boardIdx);
		
		List<Integer> uploadIdxList = modifyBoardDto.getUploadIdxList();
		
		if (uploadIdxList != null) {
			uploadMapper.activateImage(boardIdx, uploadIdxList);
		}
	}

	@Override
	public int getBoardCategoryCode(int boardIdx) {
		return boardMapper.findBoardCategoryCode(boardIdx);
	}

	@Override
	public void deleteBoard(int boardIdx) {
		boardMapper.deleteBoard(boardIdx);
	}

}
