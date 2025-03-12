package com.codingjoa.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDetailsDto {

	private int id;
	private int categoryCode;
	private String title;
	private String content;
	private int viewCount;
	private int replyCount;
	private int likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String nickname;
	private boolean isWriter;
	private boolean isLiked;		
	
	@Builder
	private BoardDetailsDto(int id, int categoryCode, String title, String content, int viewCount, int replyCount,
			int likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, String nickname, boolean isWriter,
			boolean isLiked) {
		this.id = id;
		this.categoryCode = categoryCode;
		this.title = title;
		this.content = content;
		this.viewCount = viewCount;
		this.replyCount = replyCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.nickname = nickname;
		this.isWriter = isWriter;
		this.isLiked = isLiked;
	}
	
	public static BoardDetailsDto from(Map<String, Object> map) {
		return BoardDetailsDto.builder()
				.id((int) map.get("id"))
				.categoryCode((int) map.get("categoryCode"))
				.title((String) map.get("title"))
				.content((String) map.get("content"))
				.viewCount((int) map.get("viewCount"))
				.replyCount((int) map.get("replyCount"))
				.likeCount((int) map.get("likeCount"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.nickname((String) map.get("nickname"))
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

	@Override
	public String toString() {
		return "BoardDetailsDto [id=" + id + ", categoryCode=" + categoryCode + ", title=" + title + ", content="
				+ escapeContent() + ", viewCount=" + viewCount + ", replyCount=" + replyCount + ", likeCount=" + likeCount
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", nickname=" + nickname + ", isWriter="
				+ isWriter + ", isLiked=" + isLiked + "]";
	}

	
	
	


	
}
