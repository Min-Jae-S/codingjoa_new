package com.codingjoa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class AdminUserAddrDto {
	
	private String zipcode;
	private String addr;
	private String addrDetail;
	
}
