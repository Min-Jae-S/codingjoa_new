package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	id              NUMBER,
	user_id         NUMBER              NOT NULL,
    sns_id          VARCHAR2(200)       NOT NULL,
	provider        VARCHAR2(20)        NOT NULL,
	linked_at    	DATE                NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class SnsInfo {

	private Long id;
	private Long userId;
	private String snsId;
	private String provider;
	private LocalDateTime linkedAt;
	
	@Builder
	private SnsInfo(Long id, Long userId, String snsId, String provider, LocalDateTime linkedAt) {
		this.id = id;
		this.userId = userId;
		this.snsId = snsId;
		this.provider = provider;
		this.linkedAt = linkedAt;
	}
	
}
