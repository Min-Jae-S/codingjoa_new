package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    id                  NUMBER,
    user_id             NUMBER                      NOT NULL,
    category_code       NUMBER                      NOT NULL,
	title               VARCHAR2(500)               NOT NULL,
    content             CLOB                        NOT NULL,
    search_content      CLOB                        NULL,  
    view_count          NUMBER          DEFAULT 0   NOT NULL,   
    reply_count         NUMBER          DEFAULT 0   NOT NULL, 
    like_count          NUMBER          DEFAULT 0   NOT NULL,
	created_at          DATE                        NOT NULL,
    updated_at          DATE                        NOT NULL,
*/

@Getter
@NoArgsConstructor // for mybatis resultSet
public class Board {

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
	
	@Builder
	private Board(Long id, Long userId, Integer categoryCode, String title, String content, String searchContent,
			Integer viewCount, Integer commentCount, Integer likeCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.userId = userId;
		this.categoryCode = categoryCode;
		this.title = title;
		this.content = content;
		this.searchContent = searchContent;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		String escapedContent = (content != null) ? content.replace("\r\n", "\\r\\n") : null;
		return "Board [id=" + id + ", userId=" + userId + ", categoryCode=" + categoryCode + ", title=" + title
				+ ", content=" + escapedContent + ", searchContent=" + searchContent + ", viewCount=" + viewCount
				+ ", commentCount=" + commentCount + ", likeCount=" + likeCount + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
}
