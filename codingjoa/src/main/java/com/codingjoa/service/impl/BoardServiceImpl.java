package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.WriteBoardDto;
import com.codingjoa.entity.Board;
import com.codingjoa.entity.Upload;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public int uploadTempImage(String uploadFilename, int memberIdx) {
		Upload upload = new Upload();
		upload.setUploadFile(uploadFilename);
		upload.setUploadIdx(memberIdx);
		upload.setUploadUse(false);
		
		return boardMapper.insertUpload(upload);
	}

	@Override
	public void writeBoard(WriteBoardDto writeBoardDto) {
		Board board = modelMapper.map(writeBoardDto, Board.class);
		log.info("{}", board);
		
		boardMapper.insertBoard(board);
		
		// upload_use: false --> true
	}
	
	
}
