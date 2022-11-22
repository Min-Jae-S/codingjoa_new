package com.codingjoa.dto;

import lombok.Data;

@Data
public class FindPasswordDto {
	
	private String memberId;
	private String memberEmail;
	private String authCode;
	
}
