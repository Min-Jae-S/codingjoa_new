package com.codingjoa.service.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.test.TestRestApiMapper;
import com.codingjoa.test.TestMember;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestRestApiService {
	
	@Autowired
	private TestRestApiMapper mapper;

	public List<TestMember> read() {
		log.info("## read");
		return mapper.findMembers();
	}

	public TestMember readById(String id) {
		log.info("## readById");
		return mapper.findMemberById(id);
	}

	public void create() {
		log.info("## create");
	}

	public void update() {
		log.info("## update");
	}
	
	public void delete() {
		log.info("## delete");
	}
}
