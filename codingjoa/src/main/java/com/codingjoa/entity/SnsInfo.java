package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	id              NUMBER,
	user_id         NUMBER              NOT NULL,
    email           VARCHAR2(200)       NOT NULL,
	provider        VARCHAR2(20)        NOT NULL,
    connected_at    DATE                NULL,
	created_at      DATE                NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class SnsInfo {

	private Long id;
	private Long userId;
	private String email;
	private String provider;
	private LocalDateTime connectedAt;
	private LocalDateTime createdAt;
	
	@Builder
	private SnsInfo(Long id, Long userId, String email, String provider, LocalDateTime connectedAt,
			LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.email = email;
		this.provider = provider;
		this.connectedAt = connectedAt;
		this.createdAt = createdAt;
	}
	
}
