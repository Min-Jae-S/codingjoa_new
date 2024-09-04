package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	sns_info_idx            NUMBER,
	member_idx              NUMBER              NOT NULL,
    sns_id                  VARCHAR2(200)       NOT NULL,
	sns_provider            VARCHAR2(20)        NOT NULL,
    connected_at            DATE                NULL,
	created_at              DATE                NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class SnsInfo {

	private Integer snsInfoIdx;
	private Integer memberIdx;
	private String snsId;
	private String snsProvider;
	private LocalDateTime connectedAt;
	private LocalDateTime createdAt;
	
	@Builder
	private SnsInfo(Integer snsInfoIdx, Integer memberIdx, String snsId, String snsProvider, LocalDateTime connectedAt) {
		this.snsInfoIdx = snsInfoIdx;
		this.memberIdx = memberIdx;
		this.snsId = snsId;
		this.snsProvider = snsProvider;
		this.connectedAt = connectedAt;
	}
}
