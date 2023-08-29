package com.codingjoa.scheduler.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.ImageMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchedulerService {

	@Autowired
	private ImageMapper imageMapper;
	
	// return void & no parameter
	@Scheduled(cron = "0/15 * * * * ?") 
	public void run() {
		log.info("===========================================================================");
		log.info("## SchedulerService run [{}]", Thread.currentThread().getName());
		
		List<Integer> tempBoardImageIndexs = imageMapper.findTempBoardImages().stream()
			.map(boardImage -> boardImage.getBoardImageIdx())
			.sorted()
			.collect(Collectors.toList());
		log.info("\t > temp board  image indexs = {}", tempBoardImageIndexs);

		List<Integer> tempMemberImageIndexs = imageMapper.findTempMemberImages().stream()
				.map(memberImage -> memberImage.getMemberImageIdx())
				.sorted()
				.collect(Collectors.toList());
		log.info("\t > temp member image indexs = {}", tempMemberImageIndexs);
		log.info("===========================================================================");
	}
}
