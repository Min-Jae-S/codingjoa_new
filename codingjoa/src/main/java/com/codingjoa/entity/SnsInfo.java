package com.codingjoa.entity;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	sns_idx                 NUMBER,
	member_idx              NUMBER              NOT NULL,
	sns_provider            VARCHAR2(10)        NOT NULL,
	created_at              DATE                NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class SnsInfo {

	private Integer snsIdx;
	private Integer memberIdx;
	private String snsProvider;
	private Date createdAt;
	
	@Builder
	private SnsInfo(Integer snsIdx, Integer memberIdx, String snsProvider) {
		this.snsIdx = snsIdx;
		this.memberIdx = memberIdx;
		this.snsProvider = snsProvider;
	}
}
