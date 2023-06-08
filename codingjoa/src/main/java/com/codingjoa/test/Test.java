package com.codingjoa.test;

import com.codingjoa.annotation.AnnoTest;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
public class Test {
	
	private String param1;
	
	@AnnoTest
	private int param2;
	
	public Test() {
		log.info("## Test constructor");
	}
	
	public void setParam1(String param1) {
		log.info("## Test#setParam1, param1 = {}", param1);
		this.param1 = param1;
	}

	public void setParam2(int param2) {
		log.info("## Test#setParam2, param2 = {}", param2);
		this.param2 = param2;
	}
}
