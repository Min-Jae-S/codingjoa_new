package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReplyMapper {

	List<Map<String, Object>> findPagedReply(); 
}
