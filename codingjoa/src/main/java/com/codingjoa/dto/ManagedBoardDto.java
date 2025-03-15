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
public class ManagedBoardDto {
	
	// board
	private long id;
	private String title;
	private int viewCount;
	private int replyCount;
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
	private ManagedBoardDto(long id, String title, int viewCount, int replyCount, int likeCount, LocalDateTime createdAt,
			LocalDateTime updatedAt, int categoryCode, String categoryName, long writerId, String writerEmail, String writerNickname,
			boolean isUpdated) {
		this.id = id;
		this.title = title;
		this.viewCount = viewCount;
		this.replyCount = replyCount;
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
	
	public static ManagedBoardDto from(AdminBoard adminBoard) {
		return ManagedBoardDto.builder()
				.id(adminBoard.getId())
				.title(adminBoard.getTitle())
				.viewCount(adminBoard.getViewCount())
				.replyCount(adminBoard.getReplyCount())
				.likeCount(adminBoard.getLikeCount())
				.createdAt(adminBoard.getCreatedAt())
				.updatedAt(adminBoard.getUpdatedAt())
				.categoryCode(adminBoard.getCategory().getCode())
				.categoryName(adminBoard.getCategory().getName())
				.writerId(adminBoard.getWriter().getId())
				.writerEmail(adminBoard.getWriter().getEmail())
				.writerNickname(adminBoard.getWriter().getNickname())
				.build();
	}

	@JsonProperty("isUpdated")
	public boolean isUpdated() {
		return this.isUpdated;
	}
	
}
