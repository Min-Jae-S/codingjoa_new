package com.codingjoa.test;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.codingjoa.annotation.BoardCategoryCode;

import lombok.Data;

@Data
public class Test {
	
	@NotEmpty(message = "빈값은 허용하지 않아요.")
	private String param1;
	
	@Positive(message = "숫자는 양수여야 합니다.")
	private int param2;
	
	@BoardCategoryCode(message = "게시판 카테고리가 아닙니다.")
	private int param3;
	
	private int param4;
}
