package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Auth;

@Mapper
public interface AuthMapper {
	
	boolean insertAuth(Auth auth);

	List<String> findRolesByUserId(Long userId);
	
}
