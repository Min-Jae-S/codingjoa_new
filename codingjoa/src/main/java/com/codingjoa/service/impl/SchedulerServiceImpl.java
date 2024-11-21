package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;

import com.codingjoa.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

	@Override
	public void execute() {
		log.info("## execute");
	}
}
