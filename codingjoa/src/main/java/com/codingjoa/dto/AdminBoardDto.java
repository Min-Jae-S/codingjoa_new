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
	
	private boolean isUpdated;
	
	// user
	private long writerId;
	private String writerEmail;
	private String writerNickname;
	
	// category
	private int categoryCode;
	private String categoryName;
	
	@Builder
	private AdminBoardDto(long id, String title, int viewCount, int commentCount, int likeCount, LocalDateTime createdAt,
			LocalDateTime updatedAt, boolean isUpdated, long writerId, String writerEmail, String writerNickname,
			int categoryCode, String categoryName) {
		this.id = id;
		this.title = title;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.isUpdated = createdAt.isEqual(updatedAt);;
		this.writerId = writerId;
		this.writerEmail = writerEmail;
		this.writerNickname = writerNickname;
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
	}
	
	public static AdminBoardDto from(AdminBoard adminBoard) {
		return AdminBoardDto.builder()
				.id(adminBoard.getBoard().getId())
				.title(adminBoard.getBoard().getTitle())
				.viewCount(adminBoard.getBoard().getViewCount())
				.commentCount(adminBoard.getBoard().getCommentCount())
				.likeCount(adminBoard.getBoard().getLikeCount())
				.createdAt(adminBoard.getBoard().getCreatedAt())
				.updatedAt(adminBoard.getBoard().getUpdatedAt())
				.writerId(adminBoard.getUser().getId())
				.writerEmail(adminBoard.getUser().getEmail())
				.writerNickname(adminBoard.getUser().getNickname())
				.categoryCode(adminBoard.getCategory().getCode())
				.categoryName(adminBoard.getCategory().getName())
				.build();
	}

	@JsonProperty("isUpdated")
	public boolean isUpdated() {
		return this.isUpdated;
	}
	
}
