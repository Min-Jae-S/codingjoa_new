package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	id              NUMBER,
	user_id         NUMBER              NOT NULL,
	name            VARCHAR2(200)       NOT NULL,
    path            VARCHAR2(200)       NOT NULL,
	created_at      DATE                NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class UserImage {
	
	private Long id;
	private Long userId;
	private String name;
	private String path;
	private LocalDateTime createdAt;
	
	@Builder
	private UserImage(Long id, Long userId, String name, String path, LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.path = path;
		this.createdAt = createdAt;
	}
	
}
