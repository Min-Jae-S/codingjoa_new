package com.codingjoa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Upload;
import com.codingjoa.mapper.UploadMapper;
import com.codingjoa.service.UploadService;

@Transactional
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
	public void activateImage(BoardDto boardDto) {
		List<Integer> uploadIdxList = boardDto.getUploadIdxList();
		if (CollectionUtils.isEmpty(uploadIdxList)) return;
		
		uploadMapper.activateImage(boardDto.getBoardIdx(), uploadIdxList);
	}
	
	@Override
	public List<Integer> getUploadIdxList(int uploadBoardIdx) {
		return uploadMapper.findUploadIdxList(uploadBoardIdx);
	}
	
	@Override
	public void deactivateImage(BoardDto boardDto) {
		uploadMapper.deactivateImage(boardDto.getBoardIdx());
	}
}
