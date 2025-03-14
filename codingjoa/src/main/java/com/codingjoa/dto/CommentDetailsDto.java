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

	private long id;
	private String content;
	private boolean status;
	private int likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String writerNickname;		// INNER JOIN (user)
	private String writerImagePath;		// LEFT OUTER JOIN (user_image)
	private boolean isWriter;
	private boolean isLiked;	
	private boolean isBoardWriter;
	
	@Builder
	private CommentDetailsDto(long id, String content, boolean status, int likeCount, LocalDateTime createdAt,
			LocalDateTime updatedAt, String writerNickname, String writerImagePath, boolean isWriter, boolean isLiked,
			boolean isBoardWriter) {
		this.id = id;
		this.content = content;
		this.status = status;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.writerNickname = writerNickname;
		this.writerImagePath = writerImagePath;
		this.isWriter = isWriter;
		this.isLiked = isLiked;
		this.isBoardWriter = isBoardWriter;
	}
	
	public static CommentDetailsDto from(Map<String, Object> map) {
		return CommentDetailsDto.builder()
				.id((int) map.get("id"))
				.content((String) map.get("content"))
				.status((boolean) map.get("status"))
				.likeCount((int) map.get("likeCount"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.writerNickname((String) map.get("writerNickname"))
				.writerImagePath((String) map.get("writerImagePath"))
				.isWriter((boolean) map.get("isWriter"))
				.isLiked((boolean) map.get("isLiked"))
				.isBoardWriter((boolean) map.get("isBoardWriter"))
				.build();
	}

	@JsonIgnore
	public boolean isStatus() {
		return this.status;
	}
	
	@JsonProperty("isWriter")
	public boolean isWriter() {
		return this.isWriter;
	}

	@JsonProperty("isLiked")
	public boolean isLiked() {
		return this.isLiked;
	}
	
	@JsonProperty("isBoardWriter")
	public boolean isBoardWriter() {
		return this.isBoardWriter;
	}
	
	public String getCreatedAt() {
		return format(this.createdAt);
	}

	public String getUpdatedAt() {
		return format(this.updatedAt);
	}
	
	private String format(LocalDateTime dateTime) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDate today = LocalDate.now();
		
		return dateTime.toLocalDate().isEqual(today) ? dateTime.format(timeFormatter) : dateTime.format(dateFormatter);
	}
	
	private String escapeContent() {
		return (content != null) ? content.replace("\r\n", "\\r\\n") : null;
	}

	@Override
	public String toString() {
		return "ReplyDetailsDto [id=" + id + ", content=" + escapeContent() + ", status=" + status + ", likeCount=" + likeCount
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", writerNickname=" + writerNickname
				+ ", writerImagePath=" + writerImagePath + ", isWriter=" + isWriter + ", isLiked=" + isLiked
				+ ", isBoardWriter=" + isBoardWriter + "]";
	}

	
	
}
