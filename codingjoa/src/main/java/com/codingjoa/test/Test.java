package com.codingjoa.test;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.codingjoa.annotation.BoardCategoryCode;

import lombok.Data;

@Data
public class Test {
	
	@NotEmpty
	private String param1;
	
	@Positive
	private int param2;
	
	@BoardCategoryCode
	private int param3;
	private int param4;
	
}
