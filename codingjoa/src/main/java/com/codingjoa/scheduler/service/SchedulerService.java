package com.codingjoa.scheduler.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	@Scheduled(cron = "0/30 * * * * ?") 
	public void test() {
		log.info("===========================================================================");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		log.info("## SchedulerService test on: {}   [{}]", LocalDateTime.now().format(dtf), Thread.currentThread().getName());
		logBoardImage();
		logMemberImage();
		log.info("===========================================================================");
	}
	
	private void logBoardImage() {
		List<Integer> tempBoardImageIndexs = imageMapper.findTempBoardImages().stream()
				.map(boardImage -> boardImage.getBoardImageIdx())
				.sorted()
				.collect(Collectors.toList());
		log.info("\t > temp board  image indexes = {}", tempBoardImageIndexs);
	}
	
	private void logMemberImage() {
		List<Integer> tempMemberImageIndexs = imageMapper.findTempMemberImages().stream()
				.map(memberImage -> memberImage.getMemberImageIdx())
				.sorted()
				.collect(Collectors.toList());
		log.info("\t > temp member image indexes = {}", tempMemberImageIndexs);
	}
}
