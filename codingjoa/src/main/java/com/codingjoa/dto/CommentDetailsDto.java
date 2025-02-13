package com.codingjoa.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDetailsDto {

	private int commentIdx;
	private String commentContent;
	private boolean isCommentInUse;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String commentWriterNickname;	// from INNER JOIN with member
	private String commentWriterImageUrl;	// from LEFT OUTER JOIN with member_image
	private int commentLikesCnt;			// from LEFT OUTER JOIN with comment_likes
	private boolean isBoardWriter;
	private boolean isCommentWriter;
	private boolean isCommentLiked;	
	
	@Builder
	private CommentDetailsDto(int commentIdx, String commentContent, boolean isCommentInUse, LocalDateTime createdAt,
			LocalDateTime updatedAt, boolean isCommentWriter, boolean isBoardWriter, String commentWriterNickname,
			String commentWriterImageUrl, int commentLikesCnt, boolean isCommentLiked) {
		this.commentIdx = commentIdx;
		this.commentContent = commentContent;
		this.isCommentInUse = isCommentInUse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.commentWriterNickname = commentWriterNickname;
		this.commentWriterImageUrl = commentWriterImageUrl;
		this.commentLikesCnt = commentLikesCnt;
		this.isBoardWriter = isBoardWriter;
		this.isCommentWriter = isCommentWriter;
		this.isCommentLiked = isCommentLiked;
	}
	
	public static CommentDetailsDto from(Map<String, Object> map) {
		return CommentDetailsDto.builder()
				.commentIdx((int) map.get("commentIdx"))
				.commentContent((String) map.get("commentContent"))
				.isCommentInUse((boolean) map.get("commentUse"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.commentWriterNickname((String) map.get("commentWriterNickname"))
				.commentWriterImageUrl((String) map.get("commentWriterImageUrl"))
				.commentLikesCnt((int) map.get("commentLikesCnt"))
				.isBoardWriter((boolean) map.get("isBoardWriter"))
				.isCommentWriter((boolean) map.get("isCommentWriter"))
				.isCommentLiked((boolean) map.get("isCommentLiked"))
				.build();
	}
	
	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentDetailsDto [commentIdx=" + commentIdx + ", commentContent=" + escapedCommentContent
				+ ", isCommentInUse=" + isCommentInUse + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", commentWriterNickname=" + commentWriterNickname + ", commentWriterImageUrl="
				+ commentWriterImageUrl + ", commentLikesCnt=" + commentLikesCnt + ", isBoardWriter=" + isBoardWriter
				+ ", isCommentWriter=" + isCommentWriter + ", isCommentLiked=" + isCommentLiked + "]";
	}
	
	@JsonIgnore
	public boolean isCommentInUse() {
		return isCommentInUse;
	}
	
	@JsonProperty("isBoardWriter")
	public boolean isBoardWriter() {
		return isBoardWriter;
	}

	@JsonProperty("isCommentWriter")
	public boolean isCommentWriter() {
		return isCommentWriter;
	}

	@JsonProperty("isCommentLiked")
	public boolean isCommentLiked() {
		return isCommentLiked;
	}
	
	public String getCreatedAt() {
		return format(this.createdAt);
	}

	public String getUpdatedAt() {
		return format(this.updatedAt);
	}
	
	private String format(LocalDateTime dateTime) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalDate today = LocalDate.now();
		
		return dateTime.toLocalDate().isEqual(today) ? dateTime.format(timeFormatter) : dateTime.format(dateFormatter);
	}
	
}
