package com.codingjoa.scheduler;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobC extends QuartzJobBean {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Resource(name = "batchJob")
	private Job job;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > jobLauncher = {}", jobLauncher);
		log.info("\t > jobExplorer = {}", jobExplorer);
		//log.info("\t > batch jobs = {}", jobExplorer.getJobNames());
		
		try {
			JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
					.getNextJobParameters(this.job)
					.toJobParameters();
			log.info("\t > jobParameters = {}", jobParameters);
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
		}
	}
}
