package com.codingjoa.test;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.codingjoa.annotation.AnnoTest;
import com.codingjoa.annotation.BoardCategoryCode;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @ToString
public class Test {
	
	@NotEmpty
	private String param1;
	
	@AnnoTest
	@Positive
	private int param2;
	
	@BoardCategoryCode
	private int param3;
	
	public Test() {
		log.info("## Test#constructor");
	}
	
	public void setParam1(String param1) {
		log.info("## Test#setParam1, param1 = {}", param1);
		this.param1 = param1;
	}

	public void setParam2(int param2) {
		log.info("## Test#setParam2, param2 = {}", param2);
		this.param2 = param2;
	}

	public void setParam3(int param3) {
		log.info("## Test#setParam3, param3 = {}", param3);
		this.param3 = param3;
	}

}
