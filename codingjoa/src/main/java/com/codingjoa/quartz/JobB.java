package com.codingjoa.quartz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.codingjoa.mapper.ImageMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution // In a clustering environment, these annotations do not function
public class JobB extends QuartzJobBean {
	
	@Autowired
	private ImageMapper imageMapper;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		log.info("## {}, repeated task performed on: {}", this.getClass().getSimpleName(), LocalDateTime.now().format(dtf));
		
		List<Integer> tempBoardImageIndexs = imageMapper.findTempBoardImages().stream()
				.map(boardImage -> boardImage.getBoardImageIdx())
				.sorted()
				.collect(Collectors.toList());
		List<Integer> tempMemberImageIndexs = imageMapper.findTempMemberImages().stream()
				.map(memberImage -> memberImage.getMemberImageIdx())
				.sorted()
				.collect(Collectors.toList());
		log.info("## {}, tempBoardImages = {}, tempMemberImages = {}", 
				this.getClass().getSimpleName(), tempBoardImageIndexs, tempMemberImageIndexs);
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
