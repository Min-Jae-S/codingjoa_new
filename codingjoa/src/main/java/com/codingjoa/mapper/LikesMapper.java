package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;

@Mapper
public interface LikesMapper {
	
	void delOrInsBoardLikes(BoardLikes boardLikes); // mergeBoardLikes
	
	void delOrInsCommentLikes(CommentLikes commentLikes); // mergeCommentLikes
	
	boolean isBoardLikes(@Param("boardIdx") Integer boardIdx, @Param("memberIdx") Integer memberIdx);

	boolean isCommentLikes(@Param("commentIdx") Integer commentIdx, @Param("memberIdx") Integer memberIdx);
	
	int findBoardLikesCnt(Integer boardIdx);

	int findCommentLikesCnt(Integer boardIdx);
}
