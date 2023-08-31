package com.codingjoa.scheduler.service.impl;

import org.springframework.stereotype.Service;

import com.codingjoa.scheduler.service.QuartzService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService {@Override
	
	public void test() {
		log.info("## QuartzService test");
	}
	
}
