package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class AccountDto {
	
	private long id;
	private String email;
	private boolean hasPassword;
	private String nickname;
	private String zipcode;
	private String addr;
	private String addrDetail;
	private boolean agree;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime updatedAt;
	
	private String imagePath;
	
	@Builder
	private AccountDto(long id, String email, boolean hasPassword, String nickname, String zipcode, String addr,
			String addrDetail, boolean agree, LocalDateTime createdAt, LocalDateTime updatedAt, String imagePath) {
		this.id = id;
		this.email = email;
		this.hasPassword = hasPassword;
		this.nickname = nickname;
		this.zipcode = zipcode;
		this.addr = addr;
		this.addrDetail = addrDetail;
		this.agree = agree;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.imagePath = imagePath;
	}
	
	public static AccountDto from(Map<String, Object> map) {
		return AccountDto.builder()
				.id((long) map.get("id"))
				.email((String) map.get("email"))
				.hasPassword(map.get("password") == null ? false : true)
				.nickname((String) map.get("nickname"))
				.zipcode((String) map.get("zipcode"))
				.addr((String) map.get("addr"))
				.addrDetail((String) map.get("addrDetail"))
				.agree((boolean) map.get("agree"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.imagePath((String) map.get("imagePath"))
				.build();
	}
	
}
