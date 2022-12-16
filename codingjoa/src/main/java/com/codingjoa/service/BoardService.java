package com.codingjoa.service;

import com.codingjoa.dto.WriteBoardDto;

public interface BoardService {
	
	int uploadTempImage(String uploadFilename, int memberIdx);
	
	void writeBoard(WriteBoardDto writeBoardDto);
}
