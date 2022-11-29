package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
 	member_idx              NUMBER,
    member_id               VARCHAR2(50)    UNIQUE  NOT NULL,
    member_password         VARCHAR2(70)            NOT NULL,
    member_email            VARCHAR2(100)   UNIQUE  NOT NULL,
    member_zipcode          CHAR(5)                 NOT NULL,
    member_addr             VARCHAR2(150)           NOT NULL,
    member_addr_detail      VARCHAR2(150)           NOT NULL,
    member_agree            CHAR(1)                 NOT NULL,
    regdate                 DATE                    NOT NULL,
    moddate                 DATE                    NOT NULL,
*/

@Data
public class Member {

	private Long memberIdx;
	private String memberId;
	private String memberPassword;
	private String memberEmail;
	private String memberZipcode;
	private String memberAddr;
	private String memberAddrDetail;
	private Boolean memberAgree;
	private Date regdate;
	private Date moddate;
	
}
