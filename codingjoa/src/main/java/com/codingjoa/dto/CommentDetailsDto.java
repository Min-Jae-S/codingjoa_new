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
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime updatedAt;
	
	private boolean boardWriterMatches;
	private String memberNickname;		// from INNER JOIN with member
	private String memberImageUrl;		// from LEFT OUTER JOIN wiht member_image
	private int commentLikesCnt;		// from LEFT OUTER JOIN with comment_likes
	private boolean commentLiked;		// from LEFT OUTER JOIN with comment_likes
	
	@Builder
	private CommentDetailsDto(int commentIdx, String commentContent, boolean commentUse, LocalDateTime createdAt,
			LocalDateTime updatedAt, boolean boardWriterMatches, String memberNickname, String memberImageUrl,
			int commentLikesCnt, boolean commentLiked) {
		this.commentIdx = commentIdx;
		this.commentContent = commentContent;
		this.commentUse = commentUse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.boardWriterMatches = boardWriterMatches;
		this.memberNickname = memberNickname;
		this.memberImageUrl = memberImageUrl;
		this.commentLikesCnt = commentLikesCnt;
		this.commentLiked = commentLiked;
	}
	
	public static CommentDetailsDto from(Map<String, Object> map) {
		return CommentDetailsDto.builder()
				.commentIdx((int) map.get("commentIdx"))
				.commentContent((String) map.get("commentContent"))
				.commentUse((boolean) map.get("commentUse"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.boardWriterMatches((boolean) map.get("boardWriterMatches"))
				.memberNickname((String) map.get("memberNickname"))
				.memberImageUrl((String) map.get("memberImageUrl"))
				.commentLikesCnt((int) map.get("commentLikesCnt"))
				.commentLiked((boolean) map.get("commentLiked"))
				.build();
	}

	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentDetailsDto [commentIdx=" + commentIdx + ", commentContent=" + escapedCommentContent
				+ ", commentUse=" + commentUse + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", boardWriterMatches=" + boardWriterMatches + ", memberNickname=" + memberNickname
				+ ", memberImageUrl=" + memberImageUrl + ", commentLikesCnt=" + commentLikesCnt + ", commentLiked="
				+ commentLiked + "]";
	}
}
