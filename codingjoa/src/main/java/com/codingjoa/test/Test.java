package com.codingjoa.test;

import lombok.Data;

@Data
public class Test {
	
	private String param1;
	private int param2;
	
	public Test() {}
	
	public Test(String param1, int param2) {
		this.param1 = param1;
		this.param2 = param2;
	}
	
}
