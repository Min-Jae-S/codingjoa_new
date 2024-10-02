package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentDetailsDto {

	private int commentIdx;
	private String commentContent;
	
	@JsonIgnore
	private boolean commentUse;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime updatedAt;
	
	private String commentWriterNickname;	// from INNER JOIN with member
	private String commentWriterImageUrl;	// from LEFT OUTER JOIN wiht member_image
	private int commentLikesCnt;			// from LEFT OUTER JOIN with comment_likes
	
	@JsonProperty("isCommentWriter")
	private boolean isCommentWriter;
	
	@JsonProperty("isBoardWriter")
	private boolean isBoardWriter;
	
	@JsonProperty("isCommentLiked")
	private boolean isCommentLiked;			
	
	@Builder
	private CommentDetailsDto(int commentIdx, String commentContent, boolean commentUse, LocalDateTime createdAt,
			LocalDateTime updatedAt, boolean isCommentWriter, boolean isBoardWriter, String commentWriterNickname,
			String commentWriterImageUrl, int commentLikesCnt, boolean isCommentLiked) {
		this.commentIdx = commentIdx;
		this.commentContent = commentContent;
		this.commentUse = commentUse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.commentWriterNickname = commentWriterNickname;
		this.commentWriterImageUrl = commentWriterImageUrl;
		this.commentLikesCnt = commentLikesCnt;
		this.isCommentWriter = isCommentWriter;
		this.isBoardWriter = isBoardWriter;
		this.isCommentLiked = isCommentLiked;
	}
	
	public static CommentDetailsDto from(Map<String, Object> map, Integer memberIdx) {
		return CommentDetailsDto.builder()
				.commentIdx((int) map.get("commentIdx"))
				.commentContent((String) map.get("commentContent"))
				.commentUse((boolean) map.get("commentUse"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.commentWriterNickname((String) map.get("commentWriterNickname"))
				.commentWriterImageUrl((String) map.get("commentWriterImageUrl"))
				.commentLikesCnt((int) map.get("commentLikesCnt"))
				.isCommentWriter((boolean) map.get("isCommentWriter"))
				.isBoardWriter((boolean) map.get("isBoardWriter"))
				.isCommentLiked((boolean) map.get("isCommentLiked"))
				.build();
	}

	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentDetailsDto [commentIdx=" + commentIdx + ", commentContent=" + escapedCommentContent
				+ ", commentUse=" + commentUse + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", isCommentWriter=" + isCommentWriter + ", isBoardWriter =" + isBoardWriter
				+ ", commentWriterNickname=" + commentWriterNickname + ", commentWriterImageUrl="
				+ commentWriterImageUrl + ", commentLikesCnt=" + commentLikesCnt + ", isCommentLiked=" + isCommentLiked
				+ "]";
	}
	
}
