package com.codingjoa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class BoardImageCleanupQuartzJob extends QuartzJobBean {
	
	private final JobLauncher jobLauncher;
	private final Job boardImageCleanupJob;
	
	public BoardImageCleanupQuartzJob(JobLauncher jobLauncher, @Qualifier("boardImageCleanupBatchJob") Job boardImageCleanupJob) {
		this.boardImageCleanupJob = boardImageCleanupJob;
		this.jobLauncher = jobLauncher;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}", this.getClass().getSimpleName());
	}

}
