package com.codingjoa.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.quartz.BoardImageCleanupQuartzJob;

import lombok.RequiredArgsConstructor;

@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext,
			JobDetail boardImageCleanupJobDetail, Trigger boardImageCleanupTrigger) {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		//AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		
		schedulerFactory.setJobFactory(jobFactory);
		schedulerFactory.setAutoStartup(true);
		
		// only applies under specific conditions and may not always work when creating Jobs dynamically.
		schedulerFactory.setOverwriteExistingJobs(true);
		
		schedulerFactory.setJobDetails(boardImageCleanupJobDetail);
		schedulerFactory.setTriggers(boardImageCleanupTrigger);
		
		return schedulerFactory;
	}
	
	@Bean
	public JobDetail boardImageCleanupJobDetails() {
		return JobBuilder.newJob(BoardImageCleanupQuartzJob.class)
				.withIdentity("boardImageCleanupQuartzJob", "batchJobs")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger triggerA() {
		return TriggerBuilder.newTrigger()
				.forJob(boardImageCleanupJobDetails())
				.withIdentity("boardImageCleanupTrigger", "batchTriggers")
				//.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}
	
	@Bean
	public JobDetailFactoryBean boardImageCleanupQuartzJobDetail() {
		JobDetailFactoryBean jobFactory = new JobDetailFactoryBean();
		jobFactory.setJobClass(BoardImageCleanupQuartzJob.class);
		jobFactory.setGroup("cleanupJobs");
		jobFactory.setDescription("Board Image 정리");
		jobFactory.setDurability(true);
		return jobFactory;
	}
	
	@Bean
	public CronTriggerFactoryBean testTrigger(
			@Qualifier("boardImageCleanupQuartzJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
		triggerFactory.setJobDetail(jobDetail);
		triggerFactory.setCronExpression("0 0 3 * * ?");
		triggerFactory.setGroup("testTriggers");
		return triggerFactory;
	}

	@Bean
	public CronTriggerFactoryBean boardImageCleanupQuartzTrigger(
			@Qualifier("boardImageCleanupQuartzJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
		triggerFactory.setJobDetail(jobDetail);
		triggerFactory.setCronExpression("0 0 3 * * ?");
		triggerFactory.setGroup("cleanupTriggers");
		return triggerFactory;
	}
	
	
}
