package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.codingjoa.entity.AdminUser;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserDto {
	
	private long id;
	private String email;
	private String nickname;
	private String zipcode;
	private String addr;
	private String addrDetail;
	private boolean agree;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
	
	private List<String> roles;
	
	@Builder
	private AdminUserDto(long id, String email, String nickname, String zipcode, String addr, String addrDetail,
			boolean agree, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.zipcode = zipcode;
		this.addr = addr;
		this.addrDetail = addrDetail;
		this.agree = agree;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}	
	
	public static AdminUserDto from(AdminUser adminUser) {
		return AdminUserDto.builder().build();
	}
	
	
}
