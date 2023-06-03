package com.codingjoa.test;

import org.springframework.core.convert.converter.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConverter implements Converter<String, Test> {

	@Override
	public Test convert(String source) {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		log.info("\t > source = {}", source);
		return null;
	}

}
