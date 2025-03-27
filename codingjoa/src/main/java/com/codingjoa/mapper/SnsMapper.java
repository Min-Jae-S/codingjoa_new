package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.SnsInfo;

@Mapper
public interface SnsMapper {
	
	boolean insertSnsInfo(SnsInfo snsInfo);
	
	SnsInfo findSnsInfoByUserIdAndProvider(@Param("userId") Long userId, @Param("provider") String provider);
	
}
