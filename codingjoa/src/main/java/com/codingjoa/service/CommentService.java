package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;

public interface CommentService {

	void writeComment(CommentDto commentDto);
	
	List<CommentDetailsDto> getPagedComment();
}
