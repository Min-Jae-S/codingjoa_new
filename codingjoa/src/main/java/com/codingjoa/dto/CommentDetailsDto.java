package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
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
	
	@JsonProperty("isBoardWriter")
	private boolean isBoardWriter;

	@JsonProperty("isCommentWriter")
	private boolean isCommentWriter;
	
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
		this.isBoardWriter = isBoardWriter;
		this.isCommentWriter = isCommentWriter;
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
				.isBoardWriter(checkBoardWriter(map, memberIdx))
				.isCommentWriter(checkCommentWriter(map, memberIdx))
				.isCommentLiked(checkCommentLiked(map, memberIdx))
				.build();
	}
	
	private static boolean checkBoardWriter(Map<String, Object> map, Integer memberIdx) {
		Integer boardWriterIdx = (Integer) map.get("boardWriterIdx");
		return memberIdx.equals(boardWriterIdx);
	}

	private static boolean checkCommentWriter(Map<String, Object> map, Integer memberIdx) {
		Integer commentWriterIdx = (Integer) map.get("commentWriterIdx");
		return memberIdx.equals(commentWriterIdx);
	}
	
	@SuppressWarnings("unchecked")
	private static boolean checkCommentLiked(Map<String, Object> map, Integer memberIdx) {
		List<Integer> commentLikers = (List<Integer>) map.get("commentLikers");
		return commentLikers.contains(memberIdx);
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
