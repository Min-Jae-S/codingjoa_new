package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddrDto {
	
	@NotBlank
	private String memberZipcode;
	
	@NotBlank
	private String memberAddr;
	
	@NotBlank
	private String memberAddrDetail;
	
}
