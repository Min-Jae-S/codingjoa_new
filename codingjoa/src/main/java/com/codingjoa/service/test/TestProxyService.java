package com.codingjoa.service.test;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.annotation.AnnoTest;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
//@Transactional
//@AnnoTest
@Service
public class TestProxyService {

	@Autowired
	private TestProxyService self;
	
	@AnnoTest
	public void test() {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > self = {}", self.getClass().getName());
		log.info("\t > isAopProxy = {}", AopUtils.isAopProxy(self));
	}
	
}
