package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;

@Mapper
public interface AuthMapper {
	
	boolean insertAuth(Auth auth);

	List<String> findRolesByUserId(Long userId);
	
	boolean deleteAuthByUserIdAndRole(@Param("userId") Long userId, @Param("userId") String role);
	
}
