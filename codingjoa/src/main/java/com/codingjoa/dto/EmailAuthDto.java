package com.codingjoa.dto;

import lombok.Data;

@Data
public class EmailAuthDto {

	private String memberEmail;
	private String authCode;
}
