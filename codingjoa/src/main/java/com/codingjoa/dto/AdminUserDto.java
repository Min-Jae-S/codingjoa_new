package com.codingjoa.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime updatedAt;
	
	
	
}
