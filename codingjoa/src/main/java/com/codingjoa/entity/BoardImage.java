package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
	id              NUMBER,
	board_id        NUMBER              NULL,
	name            VARCHAR2(200)       NOT NULL,
    path            VARCHAR2(200)       NOT NULL,
	created_at      DATE                NOT NULL,
*/

@NoArgsConstructor
@Data
public class BoardImage {
	
	private Integer id;
	private Integer boardId;
	private String name;
	private String path;
	private LocalDateTime createdAt;
	
	@Builder
	private BoardImage(Integer id, Integer boardId, String name, String path, LocalDateTime createdAt) {
		this.id = id;
		this.boardId = boardId;
		this.name = name;
		this.path = path;
		this.createdAt = createdAt;
	}
	
	
}
