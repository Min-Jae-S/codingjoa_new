package com.codingjoa.test;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class TestVo {
	private Integer idx;
	private String id;
	private String name;
	private String password;
}
