package com.codingjoa.test;

import org.springframework.core.convert.converter.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConverter implements Converter<String, Integer> {

	private final int DEFAULT_VALUE = 100;
	
	@Override
	public Integer convert(String source) { // the source object to convert, which must be an instance of S (never null)
		log.info("## {}#convert ", this.getClass().getSimpleName());
		log.info("\t > source = {}", source);
		
		try {
			return Integer.parseInt(source);
		} catch (NumberFormatException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		}
		
		return DEFAULT_VALUE;
	}
}
