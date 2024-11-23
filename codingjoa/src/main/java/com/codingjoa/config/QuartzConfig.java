package com.codingjoa.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.codingjoa.quartz.AutowiringJobFactory;
import com.codingjoa.quartz.SampleJob;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.quartz")
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		AutowiringJobFactory jobFactory = new AutowiringJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		
		schedulerFactory.setJobFactory(jobFactory);
		return schedulerFactory;
	}
	
	@Bean
	public JobDetail sampleJob() {
		return JobBuilder.newJob(SampleJob.class)
				.withIdentity("sampleJob", "sampleJobs")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger sampleTrigger(@Qualifier("sampleJob") JobDetail job) {
		return TriggerBuilder.newTrigger()
				.forJob(job)
				.withIdentity("sampleTrigger", "sampleTriggers")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
	}
	
}
