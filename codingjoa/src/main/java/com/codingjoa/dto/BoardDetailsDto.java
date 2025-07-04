package com.codingjoa.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDetailsDto {

	private long id;
	private int categoryCode;
	private String title;
	private String content;
	private int viewCount;
	private int commentCount;
	private int likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String writerNickname;
	private boolean isWriter;
	private boolean isLiked;		
	
	@Builder
	private BoardDetailsDto(long id, int categoryCode, String title, String content, int viewCount, int commentCount,
			int likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, String writerNickname, boolean isWriter,
			boolean isLiked) {
		this.id = id;
		this.categoryCode = categoryCode;
		this.title = title;
		this.content = content;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.writerNickname = writerNickname;
		this.isWriter = isWriter;
		this.isLiked = isLiked;
	}
	
	public static BoardDetailsDto from(Map<String, Object> map) {
		return BoardDetailsDto.builder()
				.id((long) map.get("id"))
				.categoryCode((int) map.get("categoryCode"))
				.title((String) map.get("title"))
				.content((String) map.get("content"))
				.viewCount((int) map.get("viewCount"))
				.commentCount((int) map.get("commentCount"))
				.likeCount((int) map.get("likeCount"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.writerNickname((String) map.get("writerNickname"))
				.isWriter((boolean) map.get("isWriter"))
				.isLiked((boolean) map.get("isLiked"))
				.build();
	}
	
	public String getCreatedAt() {
		return format(this.createdAt);
	}

	public String getUpdatedAt() {
		return format(this.updatedAt);
	}
	
	public String getFullCreatedAt() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return this.createdAt.format(formatter);
	}
	
	private String format(LocalDateTime dateTime) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDate today = LocalDate.now();
		
		return dateTime.toLocalDate().isEqual(today) ? dateTime.format(timeFormatter) : dateTime.format(dateFormatter);
	}
	
	private String escapeContent() {
		return (content != null) ? content.replace("\r\n", "\\r\\n") : null;
	}
	
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	
	@Override
	public String toString() {
		return "BoardDetailsDto [id=" + id + ", categoryCode=" + categoryCode + ", title=" + title + ", content="
				+ escapeContent() + ", viewCount=" + viewCount + ", commentCount=" + commentCount + ", likeCount=" + likeCount
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", writerNickname=" + writerNickname + ", isWriter="
				+ isWriter + ", isLiked=" + isLiked + "]";
	}

	

	
}
