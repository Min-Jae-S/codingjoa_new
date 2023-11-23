package com.codingjoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.codingjoa.mapper.TestMapper;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxService {
	
	@Autowired
	private TestMapper testMapper;
	
	public void doSomething1() {
		log.info("## doSomething1");
		doSomething3();
	}
	
	@Transactional
	public void doSomething2() {
		log.info("## doSomething2");
		doSomething3();
	}

	@Transactional
	public void doSomething3() {
		log.info("## doSomething3");
		TransactionStatus status = null;
		try {
			status = TransactionAspectSupport.currentTransactionStatus();
		} catch (NoTransactionException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			log.info("\t > status = {}", status == null ? "NO TRANSACTION" : status);
		}
	}
	
	public List<TestVo> select() {
		return testMapper.select();
	}
	
	public int insert(TestVo testVo) {
		return testMapper.insert(testVo);
	}
	
	public int update(TestVo testVo) {
		return testMapper.update(testVo);
	}
	
	public int remove(Integer idx) {
		return testMapper.remove(idx);
	}
	
}
