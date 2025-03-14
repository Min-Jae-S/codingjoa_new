package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Comment;
import com.codingjoa.pagination.CommentCriteria;

@Mapper
public interface CommentMapper {
	
	boolean insertReply(Comment reply);

	List<Map<String, Object>> findPagedReplies(@Param("boardId") Long boardId,
			@Param("replyCri") CommentCriteria replyCri, @Param("memberIdx") Long userId);
	
	int findReplyTotalCnt(Long boardId);
	
	Comment findReplyById(Long replyId);
	
	boolean updateReply(Comment reply);
	
	boolean deleteReply(Comment reply);
	
}
