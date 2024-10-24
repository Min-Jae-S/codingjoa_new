package com.codingjoa.service.test;

import org.springframework.stereotype.Service;

import com.codingjoa.annotation.AnnoTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class TestProxyService {

	@AnnoTest
	public void test() {
		log.info("## {}", this.getClass().getSimpleName());
	}
	
}
