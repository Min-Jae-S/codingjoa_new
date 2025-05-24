package com.codingjoa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserImageCleanupQuartzJob extends QuartzJobBean {

	private final JobLauncher jobLauncher;
	private final Job userImageCleanupJob;
	
	public UserImageCleanupQuartzJob(JobLauncher jobLauncher, @Qualifier("userImageCleanupJob") Job userImageCleanupJob) {
		this.jobLauncher = jobLauncher;
		this.userImageCleanupJob = userImageCleanupJob;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}", this.getClass().getSimpleName());
		try {
			JobParameters params = new JobParametersBuilder()
					.addLong("timestamp", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(userImageCleanupJob, params);
		} catch (Exception e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
			throw new JobExecutionException(e);
		}
	}

}
