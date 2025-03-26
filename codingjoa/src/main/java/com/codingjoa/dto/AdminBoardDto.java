package com.codingjoa.dto;

import java.time.LocalDateTime;

import com.codingjoa.entity.AdminBoard;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminBoardDto {
	
	// board
	private long id;
	private String title;
	private int viewCount;
	private int commentCount;
	private int likeCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
	
	// category
	private int categoryCode;
	private String categoryName;
	
	// user
	private long writerId;
	private String writerEmail;
	private String writerNickname;
	
	private boolean isUpdated;
	
	@Builder
	private AdminBoardDto(long id, String title, int viewCount, int commentCount, int likeCount, LocalDateTime createdAt,
			LocalDateTime updatedAt, int categoryCode, String categoryName, long writerId, String writerEmail, String writerNickname,
			boolean isUpdated) {
		this.id = id;
		this.title = title;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.writerId = writerId;
		this.writerEmail = writerEmail;
		this.writerNickname = writerNickname;
		this.isUpdated = createdAt.isEqual(updatedAt);;
	}
	
	public static AdminBoardDto from(AdminBoard adminBoard) {
		return AdminBoardDto.builder()
				.id(adminBoard.getId())
				.title(adminBoard.getTitle())
				.viewCount(adminBoard.getViewCount())
				.commentCount(adminBoard.getCommentCount())
				.createdAt(adminBoard.getCreatedAt())
				.updatedAt(adminBoard.getUpdatedAt())
				.categoryCode(adminBoard.getCategory().getCode())
				.categoryName(adminBoard.getCategory().getName())
				.writerId(adminBoard.getUser().getId())
				.writerEmail(adminBoard.getUser().getEmail())
				.writerNickname(adminBoard.getUser().getNickname())
				.build();
	}

	@JsonProperty("isUpdated")
	public boolean isUpdated() {
		return this.isUpdated;
	}
	
}
