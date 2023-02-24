package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Comment;

@Mapper
public interface CommentMapper {
	
	void insertReply(Comment reply);

	List<Map<String, Object>> findPagedReply(); 
}
