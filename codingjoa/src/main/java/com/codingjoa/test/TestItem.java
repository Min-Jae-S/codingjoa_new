package com.codingjoa.test;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestItem {
	
	private Integer idx;
	private Integer num;
	
	@Override
	public String toString() {
		return "(idx = " + idx + ", num = " + num + ")";
	}
}
