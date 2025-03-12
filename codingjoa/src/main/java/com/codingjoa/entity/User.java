package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
    id                  NUMBER,
    email               VARCHAR2(100)   UNIQUE  NOT NULL,
    password            VARCHAR2(200)           NULL,
    nickname            VARCHAR2(50)    UNIQUE  NOT NULL,
    zipcode             CHAR(5)                 NULL,
    addr                VARCHAR2(150)           NULL,
    addr_detail         VARCHAR2(150)           NULL,
    agree               CHAR(1)                 NOT NULL,
    created_at          DATE                    NOT NULL,
    updated_at          DATE                    NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor // for mybatis resultSet
public class User {

	private Long id;
	private String email;
	private String password;
	private String nickname;
	private String zipcode;
	private String addr;
	private String addrDetail;
	private Boolean agree;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Builder
	private User(Long id, String email, String password, String nickname, String zipcode, String addr, String addrDetail,
			Boolean agree, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.zipcode = zipcode;
		this.addr = addr;
		this.addrDetail = addrDetail;
		this.agree = agree;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
}
