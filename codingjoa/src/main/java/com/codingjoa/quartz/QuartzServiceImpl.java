package com.codingjoa.quartz;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService {
	
	@Override
	public void test() {
		log.info("## QuartzService test");
	}
	
}
