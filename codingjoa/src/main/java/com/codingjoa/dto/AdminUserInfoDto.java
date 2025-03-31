package com.codingjoa.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserInfoDto {
	
	private String email;
	private String nickname;
	private String zipcode;
	private String addr;
	private String addrDetail;
	private boolean agree;
	
}
