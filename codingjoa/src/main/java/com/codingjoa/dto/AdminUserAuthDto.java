package com.codingjoa.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
public class AdminUserAuthDto {
	
	private Set<String> roles = new HashSet<>(Collections.singleton("ROLE_USER"));

	@JsonSetter
	private void setRoles(Set<String> roles) {
		log.info("## AdminUserAuthDto.setRoles");
		this.roles.addAll(roles);
		log.info("\t > roles = {}", roles);
	}
	
}
