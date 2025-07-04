package com.codingjoa.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.quartz.BoardImageCleanupQuartzJob;
import com.codingjoa.quartz.BoardSyncQuartzJob;
import com.codingjoa.quartz.CommentSyncQuartzJob;
import com.codingjoa.quartz.UserImageCleanupQuartzJob;

@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext, 
			JobDetail[] jobDetails, Trigger[] triggers) {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		//AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		
		schedulerFactory.setJobFactory(jobFactory);
		schedulerFactory.setAutoStartup(true);
		schedulerFactory.setOverwriteExistingJobs(true);
		schedulerFactory.setJobDetails(jobDetails);
		schedulerFactory.setTriggers(triggers);
		return schedulerFactory;
	}
	
	@Bean
	public JobDetail boardImageCleanupJobDetail() {
		return JobBuilder.newJob(BoardImageCleanupQuartzJob.class)
				.withIdentity("boardImageCleanupQuartzJob", "batchJobGroup")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger boardImageCleanupTrigger() {
		return TriggerBuilder.newTrigger()
				.forJob(boardImageCleanupJobDetail())
				.withIdentity("boardImageCleanupTrigger", "batchTriggerGorup")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 4 * * ?"))
				.build();
	}

	@Bean
	public JobDetail userImageCleanupJobDetail() {
		return JobBuilder.newJob(UserImageCleanupQuartzJob.class)
				.withIdentity("userImageCleanupQuartzJob", "batchJobGroup")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger userImageCleanupTrigger() {
		return TriggerBuilder.newTrigger()
				.forJob(userImageCleanupJobDetail())
				.withIdentity("boardImageCleanupTrigger", "batchTriggerGorup")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 4 * * ?"))
				.build();
	}
	
	@Bean
	public JobDetail boardSyncJobDetail() {
		return JobBuilder.newJob(BoardSyncQuartzJob.class)
				.withIdentity("boardSyncQuartzJob", "batchJobGroup")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger boardSyncTrigger() {
		return TriggerBuilder.newTrigger()
				.forJob(boardSyncJobDetail())
				.withIdentity("boardImageCleanupTrigger", "batchTriggerGorup")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 4 * * ?"))
				.build();
	}
	
	@Bean
	public JobDetail commentSyncJobDetail() {
		return JobBuilder.newJob(CommentSyncQuartzJob.class)
				.withIdentity("commentSyncJobDetail", "batchJobGroup")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger commentSyncTrigger() {
		return TriggerBuilder.newTrigger()
				.forJob(commentSyncJobDetail())
				.withIdentity("commentSyncTrigger", "batchTriggerGorup")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 4 * * ?"))
				.build();
	}
	
}
