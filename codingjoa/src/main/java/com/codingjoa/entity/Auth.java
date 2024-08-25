package com.codingjoa.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	auth_idx        NUMBER,
	member_idx      NUMBER      	NOT NULL,
	member_role     VARCHAR2(30)  	NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class Auth {

	private Integer authIdx;
	private Integer memberIdx;
	private String memberRole;
	
	@Builder
	private Auth(Integer authIdx, Integer memberIdx, String memberRole) {
		this.authIdx = authIdx;
		this.memberIdx = memberIdx;
		this.memberRole = memberRole;
	}
	
	
}
