package com.codingjoa.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data 
public class Test {
	
	private String param1;
	private int param2;
	
	public Test() {
		log.info("## Test Constructor");
		this.param1 = "aa";
		this.param2 = 10;
	}

	public void setParam1(String param1) {
		log.info("## setParam1");
		this.param1 = param1;
	}

	public void setParam2(String param2) {
		log.info("## setParam2");
		try {
			this.param2 = Integer.valueOf(param2);
		} catch (NumberFormatException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		}
	}
	
}
