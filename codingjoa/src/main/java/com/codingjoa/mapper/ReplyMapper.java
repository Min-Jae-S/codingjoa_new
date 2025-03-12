package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Reply;
import com.codingjoa.pagination.ReplyCriteria;

@Mapper
public interface ReplyMapper {
	
	boolean insertReply(Reply reply);

	List<Map<String, Object>> findPagedReplies(@Param("boardId") Long boardId,
			@Param("replyCri") ReplyCriteria replyCri, @Param("memberIdx") Long userId);
	
	int findReplyTotalCnt(Long boardId);
	
	Reply findReplyById(Long replyId);
	
	boolean updateReply(Reply reply);
	
	boolean deleteReply(Reply reply);
	
}
