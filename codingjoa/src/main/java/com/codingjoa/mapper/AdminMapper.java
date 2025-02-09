package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.BoardInfo;
import com.codingjoa.entity.CommentInfo;
import com.codingjoa.entity.MemberInfo;

@Mapper
public interface AdminMapper {
	
	List<MemberInfo> findPagedMembers();

	List<BoardInfo> findPagedBoards();

	List<CommentInfo> findPagedComments();
	
	int deleteBoards(List<Integer> boardIds);
	
}
