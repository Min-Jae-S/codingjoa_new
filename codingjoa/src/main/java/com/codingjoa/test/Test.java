package com.codingjoa.test;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class Test {
	
	@Length(min = 4, max = 8)
	@NotBlank
	private String param1;
	private int param2;
	
	public void setParam1(String param1) {
		log.info("## Test#setParam1, param1 = {}", param1);
		this.param1 = param1;
	}

	public void setParam2(int param2) {
		log.info("## Test#setParam2, param2 = {}", param2);
		this.param2 = param2;
	}
}
