package com.codingjoa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.mapper.UploadMapper;
import com.codingjoa.service.UploadService;

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
		if (CollectionUtils.isEmpty(boardImages)) { 
			return;
		}
		uploadMapper.activateBoardImage(boardDto.getBoardIdx(), boardImages);
	}
	
	@Override
	public List<Integer> getUploadIdxList(int uploadBoardIdx) {
		return uploadMapper.findUploadIdxList(uploadBoardIdx);
	}
	
	@Override
	public void deactivateBoardImage(BoardDto boardDto) {
		uploadMapper.deactivateBoardImage(boardDto.getBoardIdx());
	}
}
