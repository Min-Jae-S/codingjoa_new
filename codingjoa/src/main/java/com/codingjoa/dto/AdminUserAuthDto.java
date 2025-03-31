package com.codingjoa.dto;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserAuthDto {
	
	private List<String> roles;
	
}
