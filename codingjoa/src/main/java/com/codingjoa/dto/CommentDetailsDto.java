package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentDetailsDto {

	private int commentIdx;
	private String commentContent;
	private boolean commentUse;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime updatedAt;
	
	private String memberNickname;		// from INNER JOIN with member
	private int commentLikesCnt;		// from LEFT OUTER JOIN with comment_likes
	private boolean commentLike;		// from LEFT OUTER JOIN with comment_likes
	
	@Builder
	private CommentDetailsDto(int commentIdx, String commentContent, boolean commentUse, LocalDateTime createdAt,
			LocalDateTime updatedAt, String memberNickname, int commentLikesCnt, boolean commentLike) {
		this.commentIdx = commentIdx;
		this.commentContent = commentContent;
		this.commentUse = commentUse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.memberNickname = memberNickname;
		this.commentLikesCnt = commentLikesCnt;
		this.commentLike = commentLike;
	}
	
	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentDetailsDto [commentIdx=" + commentIdx + ", commentContent=" + escapedCommentContent + ", commentUse="
				+ commentUse + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", memberNickname="
				+ memberNickname + ", commentLikesCnt=" + commentLikesCnt + ", commentLike=" + commentLike + "]";
	}
	
	public static CommentDetailsDto from(Map<String, Object> map) {
		return CommentDetailsDto.builder()
				.commentIdx((int) map.get("commentIdx"))
				.commentContent((String) map.get("commentContent"))
				.commentUse((boolean) map.get("commentUse"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.memberNickname((String) map.get("memberNickname"))
				.commentLikesCnt((int) map.get("commentLikesCnt"))
				.commentLike((boolean) map.get("commentLike"))
				.build();
	}
	
}
