package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
	id              NUMBER,
	board_id        NUMBER                      NOT NULL,
    user_id         NUMBER                      NOT NULL,
	content         VARCHAR2(2000)              NOT NULL,
    status          CHAR(1)                     NOT NULL,
    like_count      NUMBER          DEFAULT 0   NOT NULL,       
	created_at      DATE                        NOT NULL,
    updated_at      DATE                        NOT NULL,
*/

@Getter
@NoArgsConstructor // for mybatis resultSet
public class Comment {
			
	private Long id;
	private Long boardId;
	private Long userId;
	private String content;
	private Boolean status;
	private Integer likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Builder
	private Comment(Long id, Long boardId, Long userId, String content, Boolean status,  Integer likeCount, 
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.boardId = boardId;
		this.userId = userId;
		this.content = content;
		this.status = status;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	private String escapeContent() {
		return (content != null) ? content.replace("\r\n", "\\r\\n") : null;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", boardId=" + boardId + ", userId=" + userId + ", content=" + escapeContent() + ", status="
				+ status + ", likeCount=" + likeCount + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
}
