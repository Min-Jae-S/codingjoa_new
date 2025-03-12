package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
    id          NUMBER,
    user_id     NUMBER          NOT NULL,
    role        VARCHAR2(30)    NOT NULL,
    created_at  DATE            NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class Auth {

	private Long id;
	private Long userId;
	private String role;
	private LocalDateTime createdAt;
	
	@Builder
	private Auth(Long id, Long userId, String role, LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.role = role;
		this.createdAt = createdAt;
	}
}
