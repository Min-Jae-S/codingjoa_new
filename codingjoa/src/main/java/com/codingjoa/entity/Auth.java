package com.codingjoa.entity;

import lombok.Builder;
import lombok.Data;

/*
	auth_idx        NUMBER,
	member_idx      NUMBER      	NOT NULL,
	member_role     VARCHAR2(30)  	NOT NULL,
*/

@Data
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
