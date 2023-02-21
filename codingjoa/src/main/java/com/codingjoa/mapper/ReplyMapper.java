package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Reply;

@Mapper
public interface ReplyMapper {
	
	void insertReply(Reply reply);

	List<Map<String, Object>> findPagedReply(); 
}
