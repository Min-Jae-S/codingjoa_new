package com.codingjoa.quartz;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.ImageMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService {
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private ImageMapper imageMapper;
	
	@Override
	public void test() {
		log.info("===========================================================================");
		logTempImage();
	}
	
	private void logTempImage() {
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

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}
	
}
