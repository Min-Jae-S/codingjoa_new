package com.codingjoa.service.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.mapper.test.TestRestApiMapper;
import com.codingjoa.test.TestApiRequestData;
import com.codingjoa.test.TestApiResponseData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class TestRestApiService {
	
	@Autowired
	private TestRestApiMapper mapper;

	public List<TestApiResponseData> read() {
		return mapper.findMembers();
	}

	public TestApiResponseData readById(String id) {
		return mapper.findMemberById(id);
	}

	public void create() {
		log.info("## create");
	}

	public TestApiResponseData update(TestApiRequestData requestData, String id) {
		mapper.update(requestData, id);
		return mapper.findMemberById(id);
	}
	
	public void delete() {
		log.info("## delete");
	}
}
