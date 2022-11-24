package com.codingjoa.dto;

import java.util.Map;

import lombok.Data;

@Data
public class SessionDto {

	private boolean checkPasswordResult;
	private String findAccountResult;
	private Map<String, Object> findPasswordResult;
	
}
