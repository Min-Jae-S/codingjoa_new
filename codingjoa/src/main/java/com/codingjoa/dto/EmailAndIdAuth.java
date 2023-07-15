package com.codingjoa.dto;

import lombok.Data;

@Data
public class EmailAndIdAuth {
	
	private String memberId;
	private String memberEmail;
	private String authCode;
	
}
