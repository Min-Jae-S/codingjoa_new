package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.SnsInfo;

@Mapper
public interface SnsMapper {
	
	boolean insertSnsInfo(SnsInfo snsInfo);
	
	SnsInfo findSnsInfoByUserId(Long userId);
	
}
