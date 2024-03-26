package com.codingjoa.service.test;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestRestApiService {

	public void test1() {
		log.info("## test1 - service");
	}

	public void test2() {
		log.info("## test2 - service");
	}
}
