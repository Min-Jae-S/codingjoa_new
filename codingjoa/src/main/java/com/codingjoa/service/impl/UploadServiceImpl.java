package com.codingjoa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Upload;
import com.codingjoa.mapper.UploadMapper;
import com.codingjoa.service.UploadService;

//@Transactional
@Service
public class UploadServiceImpl implements UploadService {
	
	@Autowired
	private UploadMapper uploadMapper;
	
	@Override
	public int uploadImage(String uploadFilename) {
		Upload upload = new Upload();
		upload.setUploadFile(uploadFilename);
		
		uploadMapper.insertUpload(upload);
		
		return upload.getUploadIdx();
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
	public void modifyUpload(BoardDto modifyBoardDto) {
		int boardIdx = modifyBoardDto.getBoardIdx();
		uploadMapper.deactivateImage(boardIdx);
		
		List<Integer> uploadIdxList = modifyBoardDto.getUploadIdxList();
		if (uploadIdxList != null) {
			uploadMapper.activateImage(boardIdx, uploadIdxList);
		}
	}


}
