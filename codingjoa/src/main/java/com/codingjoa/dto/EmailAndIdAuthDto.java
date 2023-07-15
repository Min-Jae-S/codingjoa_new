package com.codingjoa.dto;

import lombok.Data;

@Data
public class EmailAndIdAuthDto {
	
	private String memberId;
	private String memberEmail;
	private String authCode;
	
}
