package com.codingjoa.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.test.TestMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Service
public class TestTxPropsService {
	
	/*
	 * @@ Transaction properties
	 * 	DefaultTransactionDefinition implements TransactionDefinition { ... }
	 * 		- Transaction Propagation
	 * 		- Isolation Level
	 * 		- Timeout
	 * 		- Read Only
	 * 
	 */
	
	@Autowired
	private TestMapper mapper;
	
}
