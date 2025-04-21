package com.codingjoa.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserAuthDto {
	
	private Set<String> roles = new HashSet<>(Collections.singleton("ROLE_USER"));

	@JsonSetter
	private void setRoles(Set<String> roles) {
		this.roles.addAll(roles);
	}
	
}
