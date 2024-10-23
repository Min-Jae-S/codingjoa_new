package com.codingjoa.service.test;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.annotation.AspectTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class TestProxyService {

	@Autowired
	private TestProxyService self;

	@AspectTest
	public void test() {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > self = {}", self.getClass().getSimpleName());
		log.info("\t > isAopProxy = {}", AopUtils.isAopProxy(self));
	}
	
}
