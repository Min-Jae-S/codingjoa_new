package com.codingjoa.dto;

import com.codingjoa.enumclass.Type;

import lombok.Data;

@Data
public class EmailDto {
	
	private String memberEmail;
	private String authCode;
	private Type type;
}
