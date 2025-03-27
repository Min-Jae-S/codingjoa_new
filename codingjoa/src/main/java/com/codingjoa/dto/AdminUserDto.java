package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codingjoa.entity.AdminUser;
import com.codingjoa.entity.Auth;
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
	
	// auth
	private List<String> roles;
	
	// snsInfo
	private String provider;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime connectedAt;
	
	@Builder
	private AdminUserDto(long id, String email, String nickname, String zipcode, String addr, String addrDetail,
			boolean agree, LocalDateTime createdAt, LocalDateTime updatedAt, List<String> roles, String provider,
			LocalDateTime connectedAt) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.zipcode = zipcode;
		this.addr = addr;
		this.addrDetail = addrDetail;
		this.agree = agree;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.roles = roles;
		this.provider = provider;
		this.connectedAt = connectedAt;
	}
	
	public static AdminUserDto from(AdminUser adminUser) {
		return AdminUserDto.builder()
				.id(adminUser.getUser().getId())
				.email(adminUser.getUser().getEmail())
				.nickname(adminUser.getUser().getNickname())
				.zipcode(adminUser.getUser().getZipcode())
				.addr(adminUser.getUser().getAddr())
				.addrDetail(adminUser.getUser().getAddrDetail())
				.agree(adminUser.getUser().getAgree())
				.createdAt(adminUser.getUser().getCreatedAt())
				.updatedAt(adminUser.getUser().getUpdatedAt())
				.roles(convert(adminUser.getAuths()))
				.build();
	}
	
	private static List<String> convert(List<Auth> auths) {
		return auths.stream()
				.map(auth -> auth.getRole())
				.collect(Collectors.toList());
	}
	
}
