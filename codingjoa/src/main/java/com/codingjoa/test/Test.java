package com.codingjoa.test;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class Test {
	
	private String param1;
	private int param2;
	
	public void setParam1(String param1) {
		log.info("## {}#setParam1", this.getClass().getSimpleName());
		log.info("\t > param1 = {}", param1);
		this.param1 = param1;
	}

	public void setParam2(int param2) {
		log.info("## {}#setParam2", this.getClass().getSimpleName());
		log.info("\t > param2 = {}", param2);
		this.param2 = param2;
	}
}
