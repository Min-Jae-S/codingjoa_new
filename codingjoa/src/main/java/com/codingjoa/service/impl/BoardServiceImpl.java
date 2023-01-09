package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.entity.Upload;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.mapper.UploadMapper;
import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private UploadMapper uploadMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public int uploadTempImage(String uploadFilename) {
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
	public boolean isTempImageUploaded(int uploadIdx) {
		return uploadMapper.isTempImageUploaded(uploadIdx);
	}

	@Override
	public void activateImage(BoardDto boardDto) {
		uploadMapper.activateImage(boardDto.getBoardIdx(), boardDto.getUploadIdxList());
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
	public List<BoardDetailsDto> getBoardDetailsList(int categoryCode) {
		return boardMapper.findBoardDetailsList(categoryCode).stream()
				.map(boardDetailsMap -> modelMapper.map(boardDetailsMap, BoardDetailsDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isBoardIdxExist(int boardIdx) {
		return boardMapper.isBoardIdxExist(boardIdx);
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
	public void deactivateImage(int uploadBoardIdx) {
		uploadMapper.deactivateImage(uploadBoardIdx);
	}

	@Override
	public void modifyBoard(BoardDto modifyBoardDto) {
		Board board = modelMapper.map(modifyBoardDto, Board.class);
		log.info("modifyBoardDto ==> {}", board);
		
		boardMapper.updateBoard(board);
	}
	
	
}
