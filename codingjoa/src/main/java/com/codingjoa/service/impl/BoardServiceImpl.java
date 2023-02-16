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
import com.codingjoa.pagination.Criteria;
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
		return modelMapper.map(boardDetailsMap, BoardDetailsDto.class);
	}
	
	@Override
	public void updateBoardViews(int boardIdx) {
		boardMapper.updateBoardViews(boardIdx);
	}
	
	@Override
	public Criteria makeNewCri(Criteria cri) {
		if (!"writer".equals(cri.getType())) {
			return cri;
		}
		
		Criteria newCri = new Criteria(cri);
		String newKeyword = boardMapper.findMemberIdxByKeyword(newCri.getKeyword()).stream()
				.map(memberIdx -> memberIdx.toString())
				.collect(Collectors.joining("_"));
		log.info("newKeyword={}", newKeyword);
		
		newCri.setKeyword(newKeyword);
		
		return newCri;
	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoard(Criteria cri) {
		return boardMapper.findPagedBoard(cri).stream()
				.map(boardDetailsMap -> modelMapper.map(boardDetailsMap, BoardDetailsDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getPagination(Criteria cri) {
		int totalCnt = boardMapper.findPagedBoardTotalCnt(cri);
		return new Pagination(totalCnt, cri.getPage(), cri.getRecordCnt(), pageRange);
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
