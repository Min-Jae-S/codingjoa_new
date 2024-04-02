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
		List<TestMember> testMembers = mapper.findMembers();
		if (testMembers.size() > 0) {
			for (TestMember testMember : testMembers) {
				log.info("\t > {}", testMember);
			}
		} else {
			log.info("\t > No members");
		}
		return testMembers;
	}

	public TestMember readById(String id) {
		log.info("## readById");
		TestMember testMember = mapper.findMemberById(id);
		log.info("\t > testMember = {}", testMember);
		return testMember;
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
