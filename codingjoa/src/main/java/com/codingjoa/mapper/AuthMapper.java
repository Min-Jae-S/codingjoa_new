package com.codingjoa.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;

@Mapper
public interface AuthMapper {
	
	boolean insertAuth(Auth auth);

	Set<String> findRolesByUserId(Long userId);
	
	boolean deleteAuthByUserIdAndRole(@Param("userId") Long userId, @Param("role") String role);
	
}
