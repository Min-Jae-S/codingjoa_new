package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class AdminBoard {
	
	private Long id;
	private Long userId;
	private Integer categoryCode;
	private String title;
	private String content;
	private String searchContent;
	private Integer viewCount;
	private Integer commentCount;
	private Integer likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private User user;
	private Category category;
	
	@Override
	public String toString() {
		return "AdminBoard [id=" + id + ", userId=" + userId + ", categoryCode=" + categoryCode + ", title=" + title
				+ ", viewCount=" + viewCount + ", commentCount=" + commentCount + ", likeCount=" + likeCount
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", user=" + user + ", category=" + category
				+ "]";
	}
	
}
