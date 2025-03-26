package com.codingjoa.entity;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUser {
	
	private User user;
	private List<Auth> auths;
	private SnsInfo snsInfo;
	
}
