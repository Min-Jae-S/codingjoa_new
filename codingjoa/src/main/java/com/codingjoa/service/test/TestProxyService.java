package com.codingjoa.service.test;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class TestProxyService {

	public void test() { 
		log.info("## {}.test", this.getClass().getSimpleName());
	}
	
}
