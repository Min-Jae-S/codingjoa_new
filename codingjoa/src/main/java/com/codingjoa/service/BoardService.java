package com.codingjoa.service;

import com.codingjoa.dto.WriteBoardDto;

public interface BoardService {
	
	int uploadTempImage(String uploadFilename);
	
	void writeBoard(WriteBoardDto writeBoardDto);
	
	boolean isTmepImageUploaded(int uploadIdx);
}
