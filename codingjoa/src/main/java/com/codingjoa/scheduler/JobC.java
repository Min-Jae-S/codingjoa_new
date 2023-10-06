package com.codingjoa.scheduler;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
//@DisallowConcurrentExecution // 클러스터링 환경에선 해당 어노테이션 작동하지 않음
public class JobC extends QuartzJobBean {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Resource(name = "testJob")
	private Job job;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}", this.getClass().getSimpleName());
		try {
//			JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
//					.getNextJobParameters(this.job)
//					.toJobParameters();
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
			log.info("=================================================================");
		} catch (Exception e) {
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
		}
	}
}
