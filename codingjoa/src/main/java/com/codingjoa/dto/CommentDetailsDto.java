package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.codingjoa.util.DateTimeUtils;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentDetailsDto {

	private int commentIdx;
	private int memberIdx;
	private int boardIdx;
	private String commentContent;
	private boolean commentUse;
	private String createdAt;
	private String updatedAt;
	private String memberNickname;		// from INNER JOIN with member
	private int commentLikesCnt;		// from OUTER JOIN with comment_likes
	
	@Builder
	private CommentDetailsDto(int commentIdx, int memberIdx, int boardIdx, String commentContent, boolean commentUse,
			String createdAt, String updatedAt, String memberNickname, int commentLikesCnt) {
		this.commentIdx = commentIdx;
		this.memberIdx = memberIdx;
		this.boardIdx = boardIdx;
		this.commentContent = commentContent;
		this.commentUse = commentUse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.memberNickname = memberNickname;
		this.commentLikesCnt = commentLikesCnt;
	}
	
	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentDetailsDto [commentIdx=" + commentIdx + ", memberIdx=" + memberIdx + ", boardIdx=" + boardIdx
				+ ", commentContent=" + escapedCommentContent + ", commentUse=" + commentUse + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", memberNickname=" + memberNickname + ", commentLikesCnt="
				+ commentLikesCnt + "]";
	}

	public static CommentDetailsDto from(Map<String, Object> map) {
		LocalDateTime createdAt = (LocalDateTime) map.get("createdAt");
		LocalDateTime updatedAt = (LocalDateTime) map.get("updatedAt");
		return CommentDetailsDto.builder()
				.commentIdx((int) map.get("commentIdx"))
				.memberIdx((int) map.get("memberIdx"))
				.boardIdx((int) map.get("boardIdx"))
				.commentContent((String) map.get("commentContent"))
				.commentUse((boolean) map.get("commentUse"))
				.createdAt(DateTimeUtils.format(createdAt))
				.updatedAt(DateTimeUtils.format(updatedAt))
				.memberNickname((String) map.get("memberNickname"))
				.commentLikesCnt((int) map.get("commentLikesCnt"))
				.build();
	}
	
}
