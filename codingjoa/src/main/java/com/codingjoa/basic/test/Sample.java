package com.codingjoa.basic.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sample {
	
	private String field1;
	private String field2;
	private String field3;
	
	public static Sample create() {
		return new Sample("ab", "cd", "ef");
	}
	
}
