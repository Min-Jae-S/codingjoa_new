package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentInfoDto {

	private int commentIdx;
	private String commentContent;
	private boolean isCommentInUse;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime updatedAt;

	private String commentWriterNickname;	// from INNER JOIN with member
	private int commentLikesCnt;			// from LEFT OUTER JOIN with comment_likes
	
	@Builder
	private CommentInfoDto(int commentIdx, String commentContent, boolean isCommentInUse, LocalDateTime createdAt,
			LocalDateTime updatedAt, String commentWriterNickname, int commentLikesCnt) {
		this.commentIdx = commentIdx;
		this.commentContent = commentContent;
		this.isCommentInUse = isCommentInUse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.commentWriterNickname = commentWriterNickname;
		this.commentLikesCnt = commentLikesCnt;
	}
	
	public static CommentInfoDto from(Map<String, Object> map) {
		return CommentInfoDto.builder()
				.commentIdx((int) map.get("commentIdx"))
				.commentContent((String) map.get("commentContent"))
				.isCommentInUse((boolean) map.get("commentUse"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.commentWriterNickname((String) map.get("commentWriterNickname"))
				.commentLikesCnt((int) map.get("commentLikesCnt"))
				.build();
	}
	
	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentInfoDto [commentIdx=" + commentIdx + ", commentContent=" + escapedCommentContent + ", isCommentInUse="
				+ isCommentInUse + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", commentWriterNickname="
				+ commentWriterNickname + ", commentLikesCnt=" + commentLikesCnt + "]";
	}
	
	@JsonIgnore
	public boolean isCommentInUse() {
		return isCommentInUse;
	}
	
}
