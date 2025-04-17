package com.codingjoa.basic.test;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@NoArgsConstructor
public class TestVo {
	private Integer idx;
	private String id;
	private String name;
	private String password;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime regdate;
	
	/*
	 * /* @Builder는 빌더 클래스가 사용할 수 있도록 private 생성자를 생성하는데, 
	 * 이는 모든 필드 값을 포함한다. 이로 인해 기본 생성자가 사라지게 된다.
	 */
	@Builder 
	public TestVo(Integer idx, String id, String name, String password, LocalDateTime regdate) {
		super();
		this.idx = idx;
		this.id = id;
		this.name = name;
		this.password = password;
		this.regdate = regdate;
	}
	
}
