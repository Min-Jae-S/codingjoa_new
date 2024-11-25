package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class SchedulerServiceImpl implements SchedulerService {

	@Override
	public void execute() {
		log.info("## {}.execute", this.getClass().getSimpleName());
	}
	
}
