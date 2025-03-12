package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class AddrDto {
	
	@NotBlank
	private String zipcode;
	
	@NotBlank
	private String addr;
	
	@NotBlank
	private String addrDetail;
	
}
