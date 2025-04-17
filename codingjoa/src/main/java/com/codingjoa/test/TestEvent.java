package com.codingjoa.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TestEvent {
	
	private final String location;
	private final TestVo testVo;
}
