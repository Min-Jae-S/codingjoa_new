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
		log.info("## read");
		return mapper.findMembers();
	}

	public TestApiResponseData readById(String id) {
		log.info("## readById");
		return mapper.findMemberById(id);
	}

	public void create() {
		log.info("## create");
	}

	public int update(TestApiRequestData requestData, String id) {
		log.info("## update");
		return mapper.update(requestData, id);
	}
	
	public int delete(String id) {
		log.info("## delete");
		return mapper.delete(id);
	}
}
