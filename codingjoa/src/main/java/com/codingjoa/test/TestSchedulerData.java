package com.codingjoa.test;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TestSchedulerData {

	private Long idx;
	private String id;
	private String jobName;
	private String timestamp;
	
	@Builder
	public TestSchedulerData(Long idx, String id, String jobName, String timestamp) {
		this.idx = idx;
		this.id = id;
		this.jobName = jobName;
		this.timestamp = timestamp;
	}
	
	
}
