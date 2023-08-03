package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.mapper.UploadMapper;
import com.codingjoa.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class UploadServiceImpl implements UploadService {
	
	@Autowired
	private UploadMapper uploadMapper;
	
	@Override
	public int uploadBoardImage(String filename) {
		BoardImage boardImage = new BoardImage();
		boardImage.setBoardImageName(filename);
		uploadMapper.insertBoardImage(boardImage);
		
		return boardImage.getBoardImageIdx();
	}

	@Override
	public boolean isBoardImageUploaded(int boardImageIdx) {
		return uploadMapper.isBoardImageUploaded(boardImageIdx);
	}

	@Override
	public void activateBoardImage(BoardDto boardDto) {
		List<Integer> boardImages = boardDto.getBoardImages();
		log.info("\t > activate board images = {}", boardImages);
		
		if (CollectionUtils.isEmpty(boardImages)) { 
			return;
		}
		uploadMapper.activateBoardImage(boardImages, boardDto.getBoardIdx());
	}
	
	@Override
	public void deactivateBoardImage(BoardDto boardDto) {
		int boardIdx = boardDto.getBoardIdx();
		List<Integer> boardImages = uploadMapper.findBoardImagesByBoardIdx(boardIdx)
				.stream()
				.map(boardImage -> boardImage.getBoardImageIdx())
				.collect(Collectors.toList());
		log.info("\t > deactivate board images = {}", boardImages);
		
		uploadMapper.deactivateBoardImage(boardIdx);
	}
}
