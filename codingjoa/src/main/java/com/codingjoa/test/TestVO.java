package com.codingjoa.test;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestVo {
	private Integer idx;
	private String id;
	private String name;
	private String password;
}
