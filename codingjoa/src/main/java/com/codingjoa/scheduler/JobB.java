package com.codingjoa.scheduler;

import java.util.List;
import java.util.stream.Collectors;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.codingjoa.mapper.ImageMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
@Component
public class JobB extends QuartzJobBean {
	
	@Autowired
	private ImageMapper imageMapper;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}", this.getClass().getSimpleName());
		List<Integer> tempBoardImageIndexs = imageMapper.findTempBoardImages().stream()
				.map(boardImage -> boardImage.getBoardImageIdx())
				.sorted()
				.collect(Collectors.toList());
		log.info("\t > temp board  image indexes = {}", tempBoardImageIndexs);
		
		List<Integer> tempMemberImageIndexs = imageMapper.findTempMemberImages().stream()
				.map(memberImage -> memberImage.getMemberImageIdx())
				.sorted()
				.collect(Collectors.toList());
		log.info("\t > temp member image indexes = {}", tempMemberImageIndexs);
	}
}
