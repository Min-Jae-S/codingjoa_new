package com.codingjoa.dto;

import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberInfoDto {
	
	private int memberIdx;
	private String memberEmail;
	private boolean hasPassword;
	private String memberNickname;
	private String memberZipcode;
	private String memberAddr;
	private String memberAddrDetail;
	private boolean memberAgree;
	private Date createdAt;
	private Date updatedAt;
	private String memberImageUrl;
	
	@Builder
	private MemberInfoDto(int memberIdx, String memberEmail, boolean hasPassword, String memberNickname,
			String memberZipcode, String memberAddr, String memberAddrDetail, String memberImageUrl,
			boolean memberAgree, Date createdAt, Date updatedAt) {
		this.memberIdx = memberIdx;
		this.memberEmail = memberEmail;
		this.hasPassword = hasPassword;
		this.memberNickname = memberNickname;
		this.memberZipcode = memberZipcode;
		this.memberAddr = memberAddr;
		this.memberAddrDetail = memberAddrDetail;
		this.memberAgree = memberAgree;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.memberImageUrl = memberImageUrl;
	}
	
	public static MemberInfoDto from(Map<String, Object> map) {
		return MemberInfoDto.builder()
				.memberIdx((int) map.get("memberIdx"))
				.memberEmail((String) map.get("memberEmail"))
				.hasPassword((String) map.get("memberPassword") != null ? true : false)
				.memberNickname((String) map.get("memberNickname"))
				.memberZipcode((String) map.get("memberZipcode"))
				.memberAddr((String) map.get("memberAddr"))
				.memberAddrDetail((String) map.get("memberAddrDetail"))
				.memberAgree((boolean) map.get("memberAgree"))
				.createdAt((Date) map.get("createdAt"))
				.updatedAt((Date) map.get("updatedAt"))
				.memberImageUrl((String) map.get("memberImageUrl"))
				.build();
	}
	
	
}
