package com.codingjoa.entity;

import lombok.Data;

/*
	auth_idx        NUMBER,
	member_id       VARCHAR2(50)  UNIQUE                    NOT NULL,
	member_role     VARCHAR2(30)  DEFAULT   'ROLE_MEMBER'   NOT NULL,
*/

@Data
public class Auth {

	private Long authIdx;
	private String memberId;
	private String memberRole;
	
}
