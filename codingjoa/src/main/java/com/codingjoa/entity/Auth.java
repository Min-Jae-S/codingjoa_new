package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
    auth_idx        NUMBER,
    member_idx      NUMBER          NOT NULL,
    member_role     VARCHAR2(30)    NOT NULL,
    created_at      DATE            NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class Auth {

	private Integer authIdx;
	private Integer memberIdx;
	private String memberRole;
	private LocalDateTime createdAt;
	
	@Builder
	private Auth(Integer authIdx, Integer memberIdx, String memberRole) {
		this.authIdx = authIdx;
		this.memberIdx = memberIdx;
		this.memberRole = memberRole;
	}
	
}
