package com.codingjoa.dto;

import java.util.Set;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserAuthDto {
	
	private Set<String> roles;
	
}
