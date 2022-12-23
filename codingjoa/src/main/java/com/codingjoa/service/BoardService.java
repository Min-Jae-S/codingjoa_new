package com.codingjoa.service;

import com.codingjoa.dto.WriteBoardDto;

public interface BoardService {
	
	int uploadTempImage(String uploadFilename);
	
	int writeBoard(WriteBoardDto writeBoardDto);
	
	boolean isTempImageUploaded(int uploadIdx);
	
	void activateTempImage(WriteBoardDto writeBoardDto);
}
