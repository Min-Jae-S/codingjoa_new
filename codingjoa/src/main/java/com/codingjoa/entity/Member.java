package com.codingjoa.entity;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
    member_idx              NUMBER,
    member_email            VARCHAR2(100)   UNIQUE  NOT NULL,
    member_password         VARCHAR2(70)            NULL,
    member_nickname         VARCHAR2(50)    UNIQUE  NOT NULL,
    member_zipcode          CHAR(5)                 NULL,
    member_addr             VARCHAR2(150)           NULL,
    member_addr_detail      VARCHAR2(150)           NULL,
    member_agree            CHAR(1)                 NOT NULL,
    created_at              DATE                    NOT NULL,
    updated_at              DATE                    NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor // for mybatis resultSet
public class Member {

	private Integer memberIdx;
	private String memberEmail;
	private String memberPassword;
	private String memberNickname;
	private String memberZipcode;
	private String memberAddr;
	private String memberAddrDetail;
	private Boolean memberAgree;
	private Date createdAt;
	private Date updatedAt;
	
	@Builder
	private Member(Integer memberIdx, String memberEmail, String memberPassword, String memberNickname,
			String memberZipcode, String memberAddr, String memberAddrDetail, Boolean memberAgree) {
		this.memberIdx = memberIdx;
		this.memberEmail = memberEmail;
		this.memberPassword = memberPassword;
		this.memberNickname = memberNickname;
		this.memberZipcode = memberZipcode;
		this.memberAddr = memberAddr;
		this.memberAddrDetail = memberAddrDetail;
		this.memberAgree = memberAgree;
	}
}
