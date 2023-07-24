package com.codingjoa.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Sample {
	
	private String field1;
	private String field2;
	private String field3;
	
	@SampleAnno
	public void sampleAnno() {
		log.info("\t > sampleAnno called...");
	}
	
}
